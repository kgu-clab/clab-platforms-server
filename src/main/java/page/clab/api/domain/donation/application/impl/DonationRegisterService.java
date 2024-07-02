package page.clab.api.domain.donation.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.DonationRegisterUseCase;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class DonationRegisterService implements DonationRegisterUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ValidationService validationService;
    private final DonationRepository donationRepository;

    @Transactional
    @Override
    public Long register(DonationRequestDto requestDto) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }
}