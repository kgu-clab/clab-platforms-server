package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.response.MyProfileResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMyProfileUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MyProfileRetrievalController {

    private final RetrieveMyProfileUseCase retrieveMyProfileUseCase;

    @Operation(summary = "[G] 내 프로필 조회", description = "ROLE_GUEST 이상의 권한이 필요함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/my-profile")
    public ApiResponse<MyProfileResponseDto> retrieveMyProfile() {
        MyProfileResponseDto myProfile = retrieveMyProfileUseCase.retrieveMyProfile();
        return ApiResponse.success(myProfile);
    }
}
