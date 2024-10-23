package page.clab.api.domain.members.donation.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.members.donation.application.dto.request.DonationRequestDto;
import page.clab.api.domain.members.donation.application.dto.response.DonationResponseDto;
import page.clab.api.domain.members.donation.domain.Donation;

@Component
public class DonationDtoMapper {

    public Donation fromDto(DonationRequestDto requestDto, String memberId) {
        return Donation.builder()
                .memberId(memberId)
                .amount(requestDto.getAmount())
                .message(requestDto.getMessage())
                .isDeleted(false)
                .build();
    }

    public DonationResponseDto toDto(Donation donation, String memberName) {
        return DonationResponseDto.builder()
                .id(donation.getId())
                .memberId(donation.getMemberId())
                .memberName(memberName)
                .amount(donation.getAmount())
                .message(donation.getMessage())
                .createdAt(donation.getCreatedAt())
                .build();
    }
}
