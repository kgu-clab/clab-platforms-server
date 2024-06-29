package page.clab.api.domain.donation.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.CreateDonationService;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateDonationServiceImpl implements CreateDonationService {

    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;
    private final DonationRepository donationRepository;

    @Transactional
    @Override
    public Long execute(DonationRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }
}