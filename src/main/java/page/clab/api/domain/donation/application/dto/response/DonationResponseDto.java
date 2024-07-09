package page.clab.api.domain.donation.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.donation.domain.Donation;

import java.time.LocalDateTime;

@Getter
@Builder
public class DonationResponseDto {

    private Long id;

    private String memberId;

    private String memberName;

    private Double amount;

    private String message;

    private LocalDateTime createdAt;

    public static DonationResponseDto toDto(Donation donation, String memberName) {
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
