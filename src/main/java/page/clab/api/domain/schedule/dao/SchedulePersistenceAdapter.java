package page.clab.api.domain.schedule.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.port.out.LoadSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RemoveSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RetrieveActivitySchedulesPort;
import page.clab.api.domain.schedule.application.port.out.RetrieveCollectSchedulesPort;
import page.clab.api.domain.schedule.application.port.out.RetrieveDeletedSchedulesPort;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulesByConditionsPort;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulesWithinDateRangePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;
import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SchedulePersistenceAdapter implements
        RegisterSchedulePort,
        RemoveSchedulePort,
        LoadSchedulePort,
        RetrieveActivitySchedulesPort,
        RetrieveCollectSchedulesPort,
        RetrieveDeletedSchedulesPort,
        RetrieveSchedulesByConditionsPort,
        RetrieveSchedulesWithinDateRangePort {

    private final ScheduleRepository repository;

    @Override
    public Schedule save(Schedule schedule) {
        return repository.save(schedule);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Schedule findScheduleByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("[Schedule] id: " + id + "에 해당하는 스케줄이 존재하지 않습니다."));
    }

    @Override
    public Page<Schedule> findActivitySchedulesByDateRangeAndMember(LocalDate startDate, LocalDate endDate, Member member, Pageable pageable) {
        return repository.findActivitySchedulesByDateRangeAndMember(startDate, endDate, member, pageable);
    }

    @Override
    public ScheduleCollectResponseDto findCollectSchedules() {
        return repository.findCollectSchedules();
    }

    @Override
    public Page<Schedule> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        return repository.findByConditions(year, month, priority, pageable);
    }

    @Override
    public Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable) {
        return repository.findByDateRangeAndMember(startDate, endDate, myGroups, pageable);
    }
}
