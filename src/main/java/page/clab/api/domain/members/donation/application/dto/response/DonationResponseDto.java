package page.clab.api.domain.members.donation.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DonationResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private Double amount;
    private String message;
    private LocalDateTime createdAt;
}
