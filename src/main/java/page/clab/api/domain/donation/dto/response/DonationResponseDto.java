package page.clab.api.domain.donation.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationResponseDto {

    private Long id;

    private String donorId;

    private String name;

    private Double amount;

    private String message;

    private LocalDateTime createdAt;

    public static DonationResponseDto of(Donation donation) {
        DonationResponseDto donationResponseDto = ModelMapperUtil.getModelMapper().map(donation, DonationResponseDto.class);
        if (donation.getDonor() != null) {
            donationResponseDto.setDonorId(donation.getDonor().getId());
            donationResponseDto.setName(donation.getDonor().getName());
        }
        return donationResponseDto;
    }

}
