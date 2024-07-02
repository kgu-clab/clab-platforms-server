package page.clab.api.domain.donation.application.port.out;

import page.clab.api.domain.donation.domain.Donation;

import java.util.Optional;

public interface LoadDonationPort {
    Optional<Donation> findById(Long donationId);
    Donation findByIdOrThrow(Long donationId);
}
