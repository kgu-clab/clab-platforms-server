package page.clab.api.domain.schedule.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.ScheduleRemoveUseCase;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ScheduleRemoveService implements ScheduleRemoveUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public Long remove(Long scheduleId) throws PermissionDeniedException {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        Schedule schedule = getScheduleById(scheduleId);
        schedule.validateAccessPermission(currentMember);
        schedule.delete();
        return scheduleRepository.save(schedule).getId();
    }

    private Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("일정이 존재하지 않습니다."));
    }
}
