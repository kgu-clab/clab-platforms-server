package page.clab.api.domain.members.membershipFee.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.members.membershipFee.application.port.out.RemoveMembershipFeePort;
import page.clab.api.domain.members.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.members.membershipFee.application.port.out.UpdateMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MembershipFeePersistenceAdapter implements
        RegisterMembershipFeePort,
        RemoveMembershipFeePort,
        UpdateMembershipFeePort,
        RetrieveMembershipFeePort {

    private final MembershipFeeRepository repository;
    private final MembershipFeeMapper mapper;

    @Override
    public Optional<MembershipFee> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public MembershipFee findByIdOrThrow(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

    @Override
    public MembershipFee save(MembershipFee membershipFee) {
        MembershipFeeJpaEntity entity = mapper.toJpaEntity(membershipFee);
        MembershipFeeJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public void delete(MembershipFee membershipFee) {
        MembershipFeeJpaEntity entity = mapper.toJpaEntity(membershipFee);
        repository.delete(entity);
    }

    @Override
    public MembershipFee update(MembershipFee membershipFee) {
        MembershipFeeJpaEntity entity = mapper.toJpaEntity(membershipFee);
        MembershipFeeJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public Page<MembershipFee> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<MembershipFee> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        return repository.findByConditions(memberId, memberName, category, status, pageable)
                .map(mapper::toDomainEntity);
    }
}
