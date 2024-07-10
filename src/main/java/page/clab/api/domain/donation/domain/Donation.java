package page.clab.api.domain.donation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.donation.application.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Donation {

    private Long id;
    private String memberId;
    private Double amount;
    private String message;
    private boolean isDeleted;
    private LocalDateTime createdAt;

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
