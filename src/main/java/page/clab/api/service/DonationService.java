package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.DonationRepository;
import page.clab.api.type.dto.DonationRequestDto;
import page.clab.api.type.dto.DonationResponseDto;
import page.clab.api.type.dto.DonationUpdateRequestDto;
import page.clab.api.type.entity.Donation;
import page.clab.api.type.entity.Member;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final MemberService memberService;

    private final DonationRepository donationRepository;

    @Transactional
    public void createDonation(DonationRequestDto donationRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Donation donation = Donation.of(donationRequestDto);
        Member donor = memberService.getMemberByIdOrThrow(donationRequestDto.getDonorId());
        donation.setDonor(donor);
        donationRepository.save(donation);
    }

    public List<DonationResponseDto> getDonations() {
        List<Donation> donations = donationRepository.findAll();
        return donations.stream()
                .map(DonationResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<DonationResponseDto> searchDonation(String memberId, String name) {
        List<Donation> donations = new ArrayList<>();
        if (memberId != null) {
            donations = getDonationByDonorIdOrThrow(memberId);
        } else if (name != null) {
            donations = getDonationByDonorNameOrThrow(name);
        } else {
            throw new IllegalArgumentException("적어도 memberId 또는 name 중 하나를 제공해야 합니다.");
        }
        if (donations.isEmpty())
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return donations.stream()
                .map(DonationResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateDonation(DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Donation donation = Donation.of(donationUpdateRequestDto);
        donationRepository.save(donation);
    }

    public void deleteDonation(Long donationId) {
        donationRepository.deleteById(donationId);
    }

    private List<Donation> getDonationByDonorIdOrThrow(String memberId) {
        return donationRepository.findByDonor_Id(memberId);
    }

    private List<Donation> getDonationByDonorNameOrThrow(String name) {
        return donationRepository.findByDonor_Name(name);
    }

}
