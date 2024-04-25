package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final MemberService memberService;

    private final ValidationService validationService;

    private final DonationRepository donationRepository;

    @Transactional
    public Long createDonation(DonationRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMember);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DonationResponseDto> getDonationsByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Donation> donations = donationRepository.findByConditions(memberId, name, startDate, endDate, pageable);
        return new PagedResponseDto<>(donations.map(DonationResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DonationResponseDto> getMyDonations(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Donation> donations = getDonationsByDonor(currentMember, pageable);
        return new PagedResponseDto<>(donations.map(DonationResponseDto::toDto));
    }

    public PagedResponseDto<DonationResponseDto> getDeletedDonations(Pageable pageable) {
        Page<Donation> donations = donationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(donations.map(DonationResponseDto::toDto));
    }

    @Transactional
    public Long updateDonation(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Donation donation = getDonationByIdOrThrow(donationId);
        validateDonationUpdatePermission(currentMember);
        donation.update(donationUpdateRequestDto);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }

    public Long deleteDonation(Long donationId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Donation donation = getDonationByIdOrThrow(donationId);
        validateDonationUpdatePermission(currentMember);
        donationRepository.delete(donation);
        return donation.getId();
    }

    private Donation getDonationByIdOrThrow(Long donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("해당 후원 정보가 존재하지 않습니다."));
    }

    private Page<Donation> getDonationsByDonor(Member member, Pageable pageable) {
        return donationRepository.findByDonorOrderByCreatedAtDesc(member, pageable);
    }

    private void validateDonationUpdatePermission(Member member) throws PermissionDeniedException {
        if (!member.isSuperAdminRole()) {
            throw new PermissionDeniedException("해당 후원 정보를 수정할 권한이 없습니다.");
        }
    }

}