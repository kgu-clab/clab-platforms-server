package page.clab.api.domain.activity.activitygroup.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupReportRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupReport;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.dto.mapper.ActivityGroupDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupReportResponseDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ActivityGroupReportService {

    private final ActivityGroupAdminService activityGroupAdminService;
    private final ActivityGroupReportRepository activityGroupReportRepository;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ActivityGroupDtoMapper mapper;

    @Transactional
    public Long writeReport(ActivityGroupReportRequestDto requestDto)
        throws IllegalAccessException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        Long activityGroupId = requestDto.getActivityGroupId();
        ActivityGroup activityGroup = activityGroupAdminService.validateAndGetActivityGroupForReporting(activityGroupId,
            currentMember);
        ActivityGroupReport report = validateReportCreationPermission(requestDto, activityGroup);
        return activityGroupReportRepository.save(report).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupReportResponseDto> getReports(Long activityGroupId, Pageable pageable) {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupById(activityGroupId);
        Page<ActivityGroupReport> reports = activityGroupReportRepository.findAllByActivityGroup(activityGroup,
            pageable);
        return new PagedResponseDto<>(reports.map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    public ActivityGroupReportResponseDto searchReport(Long activityGroupId, Long turn) {
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupById(activityGroupId);
        ActivityGroupReport report = activityGroupReportRepository.findByActivityGroupAndTurn(activityGroup, turn);
        return mapper.toDto(report);
    }

    @Transactional
    public Long updateReport(Long reportId, Long activityGroupId, ActivityGroupReportUpdateRequestDto requestDto)
        throws IllegalAccessException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupById(activityGroupId);
        validateReportUpdatePermission(activityGroupId, currentMember, activityGroup);
        ActivityGroupReport report = getReportById(reportId);
        report.update(requestDto);
        return activityGroupReportRepository.save(report).getId();
    }

    public Long deleteReport(Long reportId) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupReport report = validateReportDeletionPermission(reportId, currentMember);
        activityGroupReportRepository.delete(report);
        return report.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupReportResponseDto> getDeletedActivityGroupReports(Pageable pageable) {
        Page<ActivityGroupReport> activityGroupReports = activityGroupReportRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(activityGroupReports.map(mapper::toDto));
    }

    public ActivityGroupReport getReportById(Long reportId) {
        return activityGroupReportRepository.findById(reportId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND, "활동 보고서를 찾을 수 없습니다."));
    }

    private ActivityGroupReport validateReportCreationPermission(ActivityGroupReportRequestDto requestDto,
        ActivityGroup activityGroup) {
        if (activityGroupReportRepository.existsByActivityGroupAndTurn(activityGroup, requestDto.getTurn())) {
            throw new BaseException(ErrorCode.DUPLICATE_REPORT);
        }
        return mapper.fromDto(requestDto, activityGroup);
    }

    private void validateReportUpdatePermission(Long activityGroupId, Member currentMember, ActivityGroup activityGroup)
        throws IllegalAccessException {
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(currentMember, ActivityGroupRole.LEADER,
            activityGroupId)) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 그룹의 리더만 보고서를 수정할 수 있습니다.");
        }
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 차시 보고서를 수정할 수 없습니다.");
        }
    }

    @NotNull
    private ActivityGroupReport validateReportDeletionPermission(Long reportId, Member member) {
        ActivityGroupReport report = getReportById(reportId);
        if (!activityGroupAdminService.isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER,
            report.getActivityGroup().getId())) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 그룹의 리더만 보고서를 삭제할 수 있습니다.");
        }
        return report;
    }
}
