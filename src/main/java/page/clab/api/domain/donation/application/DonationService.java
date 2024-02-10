package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SearchResultNotExistException;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final MemberService memberService;

    private final DonationRepository donationRepository;

    public Long createDonation(DonationRequestDto donationRequestDto) {
        Member member = memberService.getCurrentMember();
        Donation donation = Donation.of(donationRequestDto);
        donation.setDonor(member);
        Long id = donationRepository.save(donation).getId();
        return id;
    }

    public PagedResponseDto<DonationResponseDto> getDonations(Pageable pageable) {
        Page<Donation> donations = donationRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(donations.map(DonationResponseDto::of));
    }

    public PagedResponseDto<DonationResponseDto> getMyDonations(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Donation> donations = getDonationsByDonor(member, pageable);
        return new PagedResponseDto<>(donations.map(DonationResponseDto::of));
    }

    public PagedResponseDto<DonationResponseDto> searchDonation(String memberId, String name, Pageable pageable) {
        Page<Donation> donations;
        if (memberId != null) {
            donations = getDonationByDonorIdOrThrow(memberId, pageable);
        } else if (name != null) {
            donations = getDonationByDonorNameOrThrow(name, pageable);
        } else {
            throw new IllegalArgumentException("적어도 memberId 또는 name 중 하나를 제공해야 합니다.");
        }
        if (donations.isEmpty())
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return new PagedResponseDto<>(donations.map(DonationResponseDto::of));
    }

    public Long updateDonation(Long donationId, DonationRequestDto donationRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Donation donation = getDonationByIdOrThrow(donationId);
        if (!(donation.getDonor().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
            throw new PermissionDeniedException("해당 후원 정보를 수정할 권한이 없습니다.");
        }
        Donation updatedDonation = Donation.of(donationRequestDto);
        updatedDonation.setDonor(donation.getDonor());
        updatedDonation.setId(donation.getId());
        updatedDonation.setCreatedAt(donation.getCreatedAt());
        return donationRepository.save(updatedDonation).getId();
    }

    public Long deleteDonation(Long donationId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Donation donation = getDonationByIdOrThrow(donationId);
        if (!(donation.getDonor().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
            throw new PermissionDeniedException("해당 후원 정보를 삭제할 권한이 없습니다.");
        }
        donationRepository.deleteById(donationId);
        return donation.getId();
    }

    private Donation getDonationByIdOrThrow(Long donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("해당 후원 정보가 존재하지 않습니다."));
    }

    private Page<Donation> getDonationsByDonor(Member member, Pageable pageable) {
        return donationRepository.findByDonorOrderByCreatedAtDesc(member, pageable);
    }

    private Page<Donation> getDonationByDonorIdOrThrow(String memberId, Pageable pageable) {
        return donationRepository.findByDonor_IdOrderByCreatedAtDesc(memberId, pageable);
    }

    private Page<Donation> getDonationByDonorNameOrThrow(String name, Pageable pageable) {
        return donationRepository.findByDonor_NameOrderByCreatedAtDesc(name, pageable);
    }

}