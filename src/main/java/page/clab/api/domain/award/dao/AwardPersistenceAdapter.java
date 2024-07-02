package page.clab.api.domain.award.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.award.application.port.out.LoadAwardPort;
import page.clab.api.domain.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.award.application.port.out.RemoveAwardPort;
import page.clab.api.domain.award.application.port.out.RetrieveAwardsByConditionsPort;
import page.clab.api.domain.award.application.port.out.RetrieveDeletedAwardsPort;
import page.clab.api.domain.award.application.port.out.RetrieveMyAwardsPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AwardPersistenceAdapter implements
        RegisterAwardPort,
        LoadAwardPort,
        RemoveAwardPort,
        RetrieveAwardsByConditionsPort,
        RetrieveDeletedAwardsPort,
        RetrieveMyAwardsPort {

    private final AwardRepository awardRepository;

    @Override
    public Award save(Award award) {
        return awardRepository.save(award);
    }

    @Override
    public Optional<Award> findById(Long awardId) {
        return awardRepository.findById(awardId);
    }

    @Override
    public Award findByIdOrThrow(Long awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("[Award] id: " + awardId + "에 해당하는 수상 이력이 존재하지 않습니다."));
    }

    @Override
    public void delete(Award award) {
        awardRepository.delete(award);
    }

    @Override
    public Page<Award> findByConditions(String memberId, Long year, Pageable pageable) {
        return awardRepository.findByConditions(memberId, year, pageable);
    }

    @Override
    public Page<Award> findAllByIsDeletedTrue(Pageable pageable) {
        return awardRepository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Award> findByMemberId(String memberId, Pageable pageable) {
        return awardRepository.findByMemberId(memberId, pageable);
    }
}
