package page.clab.api.type.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotNull(message = "{notNull.donation.donorId}")
    private String donorId;

    @NotNull(message = "{notNull.donation.amount}")
    @Min(value = 1, message = "{min.donation.amount}")
    private Double amount;

    @NotNull(message = "{notNull.donation.message}")
    @Size(min = 1, max = 1000, message = "{size.donation.message}")
    private String message;

}
