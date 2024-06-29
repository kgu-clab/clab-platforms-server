package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class DeleteDonationServiceImpl implements DeleteDonationService {

    private final DonationRepository donationRepository;
    private final MemberLookupService memberLookupService;

    @Transactional
    @Override
    public Long execute(Long donationId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Donation donation = getDonationByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.delete();
        return donationRepository.save(donation).getId();
    }

    private Donation getDonationByIdOrThrow(Long donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("해당 후원 정보가 존재하지 않습니다."));
    }
}

