package page.clab.api.domain.donation.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.UpdateDonationUseCase;
import page.clab.api.domain.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.donation.application.port.out.RetrieveDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.application.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class DonationUpdateService implements UpdateDonationUseCase {

    private final RetrieveDonationPort retrieveDonationPort;
    private final RegisterDonationPort registerDonationPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long updateDonation(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Donation donation = retrieveDonationPort.findByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.update(donationUpdateRequestDto);
        validationService.checkValid(donation);
        return registerDonationPort.save(donation).getId();
    }
}
