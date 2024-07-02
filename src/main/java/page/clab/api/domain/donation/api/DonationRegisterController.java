package page.clab.api.domain.donation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.donation.application.port.in.DonationRegisterUseCase;
import page.clab.api.domain.donation.dto.request.DonationRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@Tag(name = "Donation", description = "후원")
public class DonationRegisterController {

    private final DonationRegisterUseCase donationRegisterUseCase;

    @Operation(summary = "[S] 후원 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> registerDonation(
            @Valid @RequestBody DonationRequestDto requestDto
    ) {
        Long id = donationRegisterUseCase.register(requestDto);
        return ApiResponse.success(id);
    }
}