package page.clab.api.domain.members.donation.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.donation.application.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.members.donation.application.port.in.UpdateDonationUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Members - Donation", description = "후원")
public class DonationUpdateController {

    private final UpdateDonationUseCase updateDonationUseCase;

    @Operation(summary = "[S] 후원 정보 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @PatchMapping("/{donationId}")
    public ApiResponse<Long> updateDonation(
        @PathVariable(name = "donationId") Long donationId,
        @Valid @RequestBody DonationUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = updateDonationUseCase.updateDonation(donationId, requestDto);
        return ApiResponse.success(id);
    }
}
