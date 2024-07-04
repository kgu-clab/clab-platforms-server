package page.clab.api.domain.membershipFee.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.membershipFee.application.port.out.RemoveMembershipFeePort;
import page.clab.api.domain.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.membershipFee.application.port.out.UpdateMembershipFeePort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
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

    @Override
    public Optional<MembershipFee> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public MembershipFee findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회비 내역입니다."));
    }

    @Override
    public MembershipFee save(MembershipFee membershipFee) {
        return repository.save(membershipFee);
    }

    @Override
    public void delete(MembershipFee membershipFee) {
        repository.delete(membershipFee);
    }

    @Override
    public MembershipFee update(MembershipFee membershipFee) {
        return repository.save(membershipFee);
    }

    @Override
    public Page<MembershipFee> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<MembershipFee> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        return repository.findByConditions(memberId, memberName, category, status, pageable);
    }
}
