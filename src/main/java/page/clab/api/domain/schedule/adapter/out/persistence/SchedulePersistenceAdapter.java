package page.clab.api.domain.schedule.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RemoveSchedulePort;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SchedulePersistenceAdapter implements
        RegisterSchedulePort,
        RetrieveSchedulePort,
        RemoveSchedulePort {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    @Override
    public Schedule save(Schedule schedule) {
        ScheduleJpaEntity entity = mapper.toJpaEntity(schedule);
        ScheduleJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return repository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Schedule findByIdOrThrow(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("[Schedule] id: " + id + "에 해당하는 스케줄이 존재하지 않습니다."));
    }

    @Override
    public Page<Schedule> findActivitySchedulesByDateRangeAndMemberId(LocalDate startDate, LocalDate endDate, String memberId, Pageable pageable) {
        return repository.findActivitySchedulesByDateRangeAndMemberId(startDate, endDate, memberId, pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public ScheduleCollectResponseDto findCollectSchedules() {
        return repository.findCollectSchedules();
    }

    @Override
    public Page<Schedule> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        return repository.findByConditions(year, month, priority, pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable) {
        return repository.findByDateRangeAndMember(startDate, endDate, myGroups, pageable)
                .map(mapper::toDomainEntity);
    }
}
