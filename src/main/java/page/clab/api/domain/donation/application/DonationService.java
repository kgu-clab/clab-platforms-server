package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final MemberLookupService memberLookupService;

    private final ValidationService validationService;

    private final DonationRepository donationRepository;

    @Transactional
    public Long createDonation(DonationRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Donation donation = DonationRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DonationResponseDto> getDonationsByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Donation> donations = donationRepository.findByConditions(memberId, name, startDate, endDate, pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DonationResponseDto> getMyDonations(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Donation> donations = donationRepository.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DonationResponseDto> getDeletedDonations(Pageable pageable) {
        Page<Donation> donations = donationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }

    @Transactional
    public Long updateDonation(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Donation donation = getDonationByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.update(donationUpdateRequestDto);
        validationService.checkValid(donation);
        return donationRepository.save(donation).getId();
    }

    public Long deleteDonation(Long donationId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Donation donation = getDonationByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donationRepository.delete(donation);
        return donation.getId();
    }

    private Donation getDonationByIdOrThrow(Long donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("해당 후원 정보가 존재하지 않습니다."));
    }

}