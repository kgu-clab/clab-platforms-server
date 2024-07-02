package page.clab.api.domain.donation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.donation.application.DonationRemoveUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
public class DonationRemoveController {

    private final DonationRemoveUseCase donationRemoveUseCase;

    @Operation(summary = "[S] 후원 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{donationId}")
    public ApiResponse<Long> removeDonation(
            @PathVariable(name = "donationId") Long donationId
    ) throws PermissionDeniedException {
        Long id = donationRemoveUseCase.remove(donationId);
        return ApiResponse.success(id);
    }
}