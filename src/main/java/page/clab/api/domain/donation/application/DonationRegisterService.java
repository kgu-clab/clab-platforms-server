package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.DonationRegisterUseCase;
import page.clab.api.domain.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class DonationRegisterService implements DonationRegisterUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ValidationService validationService;
    private final RegisterDonationPort registerDonationPort;

    @Transactional
    @Override
    public Long register(DonationRequestDto requestDto) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(donation);
        return registerDonationPort.save(donation).getId();
    }
}
