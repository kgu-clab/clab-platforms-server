package page.clab.api.domain.donation.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

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
        return ModelMapperUtil.getModelMapper().map(donationRequestDto, Donation.class);
    }

}