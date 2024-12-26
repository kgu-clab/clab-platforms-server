package page.clab.api.domain.members.donation.application.port.out;

import page.clab.api.domain.members.donation.domain.Donation;

public interface RegisterDonationPort {

    Donation save(Donation donation);
}
