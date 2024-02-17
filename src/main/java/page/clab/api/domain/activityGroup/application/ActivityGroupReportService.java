package page.clab.api.domain.activityGroup.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.dao.ActivityGroupReportRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupReport;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupReportResponseDto;
import page.clab.api.domain.activityGroup.exception.DuplicateReportException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ActivityGroupReportService {

    private final ActivityGroupReportRepository activityGroupReportRepository;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final MemberService memberService;

    public Long writeReport(ActivityGroupReportRequestDto reportRequestDto) throws PermissionDeniedException, IllegalAccessException {
        Member member = memberService.getCurrentMember();
        Long activityGroupId = reportRequestDto.getActivityGroupId();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);

        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 리더만 보고서를 작성할 수 있습니다.");
        }

        if (!activityGroupAdminService.isActivityGroupProgressing(activityGroupId)) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 차시 보고서를 작성할 수 없습니다.");
        }

        Long turn = reportRequestDto.getTurn();

        if (isReportExist(activityGroup, turn)) {
            throw new DuplicateReportException("이미 해당 차시의 보고서가 존재합니다.");
        }

        ActivityGroupReport report = ActivityGroupReport.builder()
                .turn(turn)
                .activityGroup(activityGroup)
                .title(reportRequestDto.getTitle())
                .content(reportRequestDto.getContent())
                .build();

        Long id = save(report);

        return id;
    }

    public PagedResponseDto<ActivityGroupReportResponseDto> getReports(Long activityGroupId, Pageable pageable){
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        Page<ActivityGroupReport> reports = getReportByActivityGroup(activityGroup, pageable);
        return new PagedResponseDto<>(reports.map(ActivityGroupReportResponseDto::of));
    }

    public ActivityGroupReportResponseDto searchReport(Long activityGroupId, Long turn){
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        ActivityGroupReport report = getReportByActivityGroupAndTurn(activityGroup, turn);
        return ActivityGroupReportResponseDto.of(report);
    }

    public Long updateReport(Long reportId, Long activityGroupId, ActivityGroupReportUpdateRequestDto reportRequestDto) throws PermissionDeniedException, IllegalAccessException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 리더만 보고서를 수정할 수 있습니다.");
        }
        if (!activityGroupAdminService.isActivityGroupProgressing(activityGroupId)) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 차시 보고서를 수정할 수 없습니다.");
        }
        ActivityGroupReport report = getReportByIdOrThrow(reportId);
        report.update(reportRequestDto);
        return save(report);
    }

    public Long deleteReport(Long reportId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroupReport report = getReportByIdOrThrow(reportId);
        ActivityGroup activityGroup = report.getActivityGroup();

        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroup.getId())) {
            throw new PermissionDeniedException("해당 그룹의 리더만 보고서를 삭제할 수 있습니다.");
        }

        activityGroupReportRepository.delete(report);
        return report.getId();
    }

    public boolean isReportExist(ActivityGroup activityGroup, Long turn){
        ActivityGroupReport report = getReportByActivityGroupAndTurn(activityGroup, turn);
        if (report != null)
            return true;
        return false;
    }

    public Long save(ActivityGroupReport report){
        return activityGroupReportRepository.save(report).getId();
    }

    public Page<ActivityGroupReport> getReportByActivityGroup(ActivityGroup activityGroup, Pageable pageable){
        return activityGroupReportRepository.findAllByActivityGroup(activityGroup, pageable);
    }

    public ActivityGroupReport getReportByActivityGroupAndTurn(ActivityGroup activityGroup, Long turn){
        return activityGroupReportRepository.findByActivityGroupAndTurn(activityGroup, turn);
    }

    public ActivityGroupReport getReportByIdOrThrow(Long reportId){
        return activityGroupReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("활동 보고서를 찾을 수 없습니다."));
    }

}
