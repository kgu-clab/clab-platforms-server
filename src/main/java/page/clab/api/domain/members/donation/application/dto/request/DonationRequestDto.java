package page.clab.api.domain.members.donation.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.members.donation.domain.Donation;

@Getter
@Setter
public class DonationRequestDto {

    @NotNull(message = "{notNull.donation.amount}")
    @Schema(description = "금액", example = "100000", required = true)
    private Double amount;

    @NotNull(message = "{notNull.donation.message}")
    @Schema(description = "후원 메시지", example = "대회 상금 일부 후원", required = true)
    private String message;

    public static Donation toEntity(DonationRequestDto requestDto, String memberId) {
        return Donation.builder()
                .memberId(memberId)
                .amount(requestDto.getAmount())
                .message(requestDto.getMessage())
                .build();
    }
}
