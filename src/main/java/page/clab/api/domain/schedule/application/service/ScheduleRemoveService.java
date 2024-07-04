package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.port.in.RemoveScheduleUseCase;
import page.clab.api.domain.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ScheduleRemoveService implements RemoveScheduleUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveSchedulePort retrieveSchedulePort;
    private final RegisterSchedulePort registerSchedulePort;

    @Override
    @Transactional
    public Long remove(Long scheduleId) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Schedule schedule = retrieveSchedulePort.findByIdOrThrow(scheduleId);
        schedule.validateAccessPermission(currentMember);
        schedule.delete();
        return registerSchedulePort.save(schedule).getId();
    }
}
