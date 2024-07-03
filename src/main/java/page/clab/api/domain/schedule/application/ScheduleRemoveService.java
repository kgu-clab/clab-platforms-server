package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.port.in.RemoveScheduleUseCase;
import page.clab.api.domain.schedule.application.port.out.LoadSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ScheduleRemoveService implements RemoveScheduleUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final LoadSchedulePort loadSchedulePort;
    private final RegisterSchedulePort registerSchedulePort;

    @Override
    @Transactional
    public Long remove(Long scheduleId) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Schedule schedule = loadSchedulePort.findScheduleByIdOrThrow(scheduleId);
        schedule.validateAccessPermission(currentMember);
        schedule.delete();
        return registerSchedulePort.save(schedule).getId();
    }
}
