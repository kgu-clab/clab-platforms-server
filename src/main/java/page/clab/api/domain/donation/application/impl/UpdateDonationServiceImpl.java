package page.clab.api.domain.donation.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.UpdateDonationService;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class UpdateDonationServiceImpl implements UpdateDonationService {

    private final DonationRepository donationRepository;
    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long execute(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Donation donation = getDonationByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.update(donationUpdateRequestDto);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }

    private Donation getDonationByIdOrThrow(Long donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("해당 후원 정보가 존재하지 않습니다."));
    }
}