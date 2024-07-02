package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.port.in.ScheduleRemoveUseCase;
import page.clab.api.domain.schedule.application.port.out.RemoveSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ScheduleRemoveService implements ScheduleRemoveUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RemoveSchedulePort removeSchedulePort;

    @Override
    @Transactional
    public Long remove(Long scheduleId) throws PermissionDeniedException {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        Schedule schedule = removeSchedulePort.findScheduleByIdOrThrow(scheduleId);
        schedule.validateAccessPermission(currentMember);
        schedule.delete();
        return removeSchedulePort.save(schedule).getId();
    }
}
