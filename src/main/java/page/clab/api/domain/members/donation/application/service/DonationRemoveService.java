package page.clab.api.domain.members.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.members.donation.application.port.in.RemoveDonationUseCase;
import page.clab.api.domain.members.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.members.donation.application.port.out.RetrieveDonationPort;
import page.clab.api.domain.members.donation.domain.Donation;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class DonationRemoveService implements RemoveDonationUseCase {

    private final RetrieveDonationPort retrieveDonationPort;
    private final RegisterDonationPort registerDonationPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional
    @Override
    public Long removeDonation(Long donationId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Donation donation = retrieveDonationPort.findByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.delete();
        return registerDonationPort.save(donation).getId();
    }
}
