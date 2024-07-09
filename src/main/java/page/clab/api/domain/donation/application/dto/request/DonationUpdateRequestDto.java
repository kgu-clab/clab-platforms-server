package page.clab.api.domain.donation.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DonationUpdateRequestDto {

    @Min(value = 1, message = "{min.donation.amount}")
    private Double amount;

    @Schema(description = "후원 메시지", example = "대회 상금 일부 후원", required = true)
    private String message;
}
