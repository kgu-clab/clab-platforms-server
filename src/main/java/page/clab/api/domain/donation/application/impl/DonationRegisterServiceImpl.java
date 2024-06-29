package page.clab.api.domain.donation.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.DonationRegisterService;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class DonationRegisterServiceImpl implements DonationRegisterService {

    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;
    private final DonationRepository donationRepository;

    @Transactional
    @Override
    public Long register(DonationRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }
}