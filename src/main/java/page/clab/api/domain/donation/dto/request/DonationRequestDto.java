package page.clab.api.domain.donation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationRequestDto {

    @NotNull(message = "{notNull.donation.amount}")
    @Min(value = 1, message = "{min.donation.amount}")
    @Schema(description = "금액", example = "100000", required = true)
    private Double amount;

    @NotNull(message = "{notNull.donation.message}")
    @Size(min = 1, max = 1000, message = "{size.donation.message}")
    @Schema(description = "후원 메시지", example = "대회 상금 일부 후원", required = true)
    private String message;

}
