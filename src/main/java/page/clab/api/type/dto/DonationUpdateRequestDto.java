package page.clab.api.type.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationUpdateRequestDto {

    private Long id;

    private String donorId;

    private Double amount;

    private String message;

}
