package page.clab.api.domain.donation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.donation.application.DonationUpdateUseCase;
import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
public class DonationUpdateController {

    private final DonationUpdateUseCase donationUpdateUseCase;

    @Operation(summary = "[S] 후원 정보 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{donationId}")
    public ApiResponse<Long> updateDonation(
            @PathVariable(name = "donationId") Long donationId,
            @Valid @RequestBody DonationUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = donationUpdateUseCase.update(donationId, requestDto);
        return ApiResponse.success(id);
    }
}
