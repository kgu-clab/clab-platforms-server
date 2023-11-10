package page.clab.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.DonationRepository;
import page.clab.api.type.dto.DonationRequestDto;
import page.clab.api.type.dto.DonationResponseDto;
import page.clab.api.type.entity.Donation;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final MemberService memberService;

    private final DonationRepository donationRepository;

    @Transactional
    public void createDonation(DonationRequestDto donationRequestDto) {
        Member member = memberService.getCurrentMember();
        Donation donation = Donation.of(donationRequestDto);
        donation.setDonor(member);
        donationRepository.save(donation);
    }

    public List<DonationResponseDto> getDonations() {
        List<Donation> donations = donationRepository.findAll();
        return donations.stream()
                .map(DonationResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<DonationResponseDto> getMyDonations() {
        Member member = memberService.getCurrentMember();
        List<Donation> donations = getDonationsByDonor(member);
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

    public void updateDonation(Long donationId, DonationRequestDto donationRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Donation donation = getDonationByIdOrThrow(donationId);
        if (!(donation.getDonor().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 후원 정보를 수정할 권한이 없습니다.");
        }
        Donation updatedDonation = Donation.of(donationRequestDto);
        updatedDonation.setDonor(donation.getDonor());
        updatedDonation.setId(donation.getId());
        updatedDonation.setCreatedAt(donation.getCreatedAt());
        donationRepository.save(updatedDonation);
    }

    public void deleteDonation(Long donationId) {
        donationRepository.deleteById(donationId);
    }

    private Donation getDonationByIdOrThrow(Long donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("해당 후원 정보가 존재하지 않습니다."));
    }

    private List<Donation> getDonationsByDonor(Member member) {
        return donationRepository.findByDonor(member);
    }

    private List<Donation> getDonationByDonorIdOrThrow(String memberId) {
        return donationRepository.findByDonor_Id(memberId);
    }

    private List<Donation> getDonationByDonorNameOrThrow(String name) {
        return donationRepository.findByDonor_Name(name);
    }

}
