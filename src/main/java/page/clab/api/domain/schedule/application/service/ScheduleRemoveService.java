package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.schedule.application.port.in.RemoveScheduleUseCase;
import page.clab.api.domain.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ScheduleRemoveService implements RemoveScheduleUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveSchedulePort retrieveSchedulePort;
    private final RegisterSchedulePort registerSchedulePort;

    @Override
    @Transactional
    public Long removeSchedule(Long scheduleId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Schedule schedule = retrieveSchedulePort.findByIdOrThrow(scheduleId);
        schedule.validateAccessPermission(currentMemberInfo);
        schedule.delete();
        return registerSchedulePort.save(schedule).getId();
    }
}
