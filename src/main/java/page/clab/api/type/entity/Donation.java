package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.type.dto.DonationRequestDto;
import page.clab.api.type.dto.DonationUpdateRequestDto;
import page.clab.api.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member donor;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.donation.amount}")
    private Double amount;

    @Column(nullable = false, length = 1000)
    @Size(min = 1, max = 1000, message = "{size.donation.message}")
    private String message;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Donation of(DonationRequestDto donationRequestDto) {
        Donation donation = Donation.builder()
                .amount(donationRequestDto.getAmount())
                .message(donationRequestDto.getMessage())
                .build();
        return donation;
    }

    public static Donation of(DonationUpdateRequestDto donationUpdateRequestDto) {
        Donation donation = ModelMapperUtil.getModelMapper().map(donationUpdateRequestDto, Donation.class);
        Member donor = new Member();
        donor.setId(donationUpdateRequestDto.getDonorId());
        donation.setDonor(donor);
        return donation;
    }

}