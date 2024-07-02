package page.clab.api.domain.donation.application.port.out;

import page.clab.api.domain.donation.domain.Donation;

public interface RegisterDonationPort {
    Donation save(Donation donation);
}