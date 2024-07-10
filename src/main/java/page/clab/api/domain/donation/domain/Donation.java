package page.clab.api.domain.donation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.donation.application.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE donation SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Donation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.donation.amount}")
    private Double amount;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.donation.message}")
    private String message;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public void update(DonationUpdateRequestDto donationUpdateRequestDto) {
        Optional.ofNullable(donationUpdateRequestDto.getAmount()).ifPresent(this::setAmount);
        Optional.ofNullable(donationUpdateRequestDto.getMessage()).ifPresent(this::setMessage);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void validateAccessPermission(boolean isSuperAdmin) throws PermissionDeniedException {
        if (!isSuperAdmin) {
            throw new PermissionDeniedException("해당 후원 정보를 수정할 권한이 없습니다.");
        }
    }
}
