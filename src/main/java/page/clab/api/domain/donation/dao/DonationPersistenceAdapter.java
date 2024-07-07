package page.clab.api.domain.donation.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.donation.application.port.out.RetrieveDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DonationPersistenceAdapter implements
        RegisterDonationPort,
        RetrieveDonationPort {

    private final DonationRepository repository;

    @Override
    public Donation save(Donation donation) {
        return repository.save(donation);
    }

    @Override
    public Page<Donation> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Donation> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return repository.findByConditions(memberId, name, startDate, endDate, pageable);
    }

    @Override
    public Optional<Donation> findById(Long donationId) {
        return repository.findById(donationId);
    }

    @Override
    public Donation findByIdOrThrow(Long donationId) {
        return repository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("[Donation] id: " + donationId + "에 해당하는 후원이 존재하지 않습니다."));
    }

    @Override
    public Page<Donation> findByMemberId(String memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable);
    }
}
