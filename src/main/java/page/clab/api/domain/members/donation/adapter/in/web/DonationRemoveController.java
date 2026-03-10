package page.clab.api.domain.members.donation.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.donation.application.port.in.RemoveDonationUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Members - Donation", description = "후원")
public class DonationRemoveController {

    private final RemoveDonationUseCase removeDonationUseCase;

    @Operation(summary = "[S] 후원 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @DeleteMapping("/{donationId}")
    public ApiResponse<Long> removeDonation(
        @PathVariable(name = "donationId") Long donationId
    ) {
        Long id = removeDonationUseCase.removeDonation(donationId);
        return ApiResponse.success(id);
    }
}
