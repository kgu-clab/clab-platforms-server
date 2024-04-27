package page.clab.api.domain.activityGroup.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.dao.ActivityGroupReportRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupReport;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupReportResponseDto;
import page.clab.api.domain.activityGroup.exception.DuplicateReportException;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ActivityGroupReportService {

    private final ActivityGroupAdminService activityGroupAdminService;

    private final MemberService memberService;

    private final ValidationService validationService;

    private final ActivityGroupReportRepository activityGroupReportRepository;

    @Transactional
    public Long writeReport(ActivityGroupReportRequestDto requestDto) throws PermissionDeniedException, IllegalAccessException {
        Member currentMember = memberService.getCurrentMember();
        Long activityGroupId = requestDto.getActivityGroupId();
        ActivityGroup activityGroup = activityGroupAdminService.validateAndGetActivityGroupForReporting(activityGroupId, currentMember);
        ActivityGroupReport report = validateReportCreationPermission(requestDto, activityGroup);
        validationService.checkValid(report);
        return activityGroupReportRepository.save(report).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupReportResponseDto> getReports(Long activityGroupId, Pageable pageable) {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        Page<ActivityGroupReport> reports = activityGroupReportRepository.findAllByActivityGroup(activityGroup, pageable);
        return new PagedResponseDto<>(reports.map(ActivityGroupReportResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public ActivityGroupReportResponseDto searchReport(Long activityGroupId, Long turn) {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        ActivityGroupReport report = activityGroupReportRepository.findByActivityGroupAndTurn(activityGroup, turn);
        return ActivityGroupReportResponseDto.toDto(report);
    }

    @Transactional
    public Long updateReport(Long reportId, Long activityGroupId, ActivityGroupReportUpdateRequestDto requestDto) throws PermissionDeniedException, IllegalAccessException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        validateReportUpdatePermission(activityGroupId, currentMember, activityGroup);
        ActivityGroupReport report = getReportByIdOrThrow(reportId);
        report.update(requestDto);
        validationService.checkValid(report);
        return activityGroupReportRepository.save(report).getId();
    }

    public Long deleteReport(Long reportId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroupReport report = validateReportDeletionPermission(reportId, currentMember);
        activityGroupReportRepository.delete(report);
        return report.getId();
    }

    public PagedResponseDto<ActivityGroupReportResponseDto> getDeletedActivityGroupReports(Pageable pageable) {
        Page<ActivityGroupReport> activityGroupReports = activityGroupReportRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(activityGroupReports.map(ActivityGroupReportResponseDto::toDto));
    }

    public ActivityGroupReport getReportByIdOrThrow(Long reportId) {
        return activityGroupReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("활동 보고서를 찾을 수 없습니다."));
    }

    private ActivityGroupReport validateReportCreationPermission(ActivityGroupReportRequestDto requestDto, ActivityGroup activityGroup) {
        if (activityGroupReportRepository.existsByActivityGroupAndTurn(activityGroup, requestDto.getTurn())) {
            throw new DuplicateReportException("이미 해당 차시의 보고서가 존재합니다.");
        }
        return ActivityGroupReportRequestDto.toEntity(requestDto, activityGroup);
    }

    private void validateReportUpdatePermission(Long activityGroupId, Member currentMember, ActivityGroup activityGroup) throws PermissionDeniedException, IllegalAccessException {
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(currentMember, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 리더만 보고서를 수정할 수 있습니다.");
        }
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 차시 보고서를 수정할 수 없습니다.");
        }
    }

    @NotNull
    private ActivityGroupReport validateReportDeletionPermission(Long reportId, Member member) throws PermissionDeniedException {
        ActivityGroupReport report = getReportByIdOrThrow(reportId);
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, report.getActivityGroup().getId())) {
            throw new PermissionDeniedException("해당 그룹의 리더만 보고서를 삭제할 수 있습니다.");
        }
        return report;
    }

}
