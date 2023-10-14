package page.clab.api.type.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationRequestDto {

    private String donorId;

    private Double amount;

    private String message;

}
