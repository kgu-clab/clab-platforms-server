package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.RemoveDonationUseCase;
import page.clab.api.domain.donation.application.port.out.LoadDonationPort;
import page.clab.api.domain.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class DonationRemoveService implements RemoveDonationUseCase {

    private final LoadDonationPort loadDonationPort;
    private final RegisterDonationPort registerDonationPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional
    @Override
    public Long remove(Long donationId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Donation donation = loadDonationPort.findByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.delete();
        return registerDonationPort.save(donation).getId();
    }
}
