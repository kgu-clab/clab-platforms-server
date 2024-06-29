package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MyProfileRetrievalService;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
public class MyProfileRetrievalController {

    private final MyProfileRetrievalService myProfileRetrievalService;

    @Operation(summary = "[U] 내 프로필 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/my-profile")
    public ApiResponse<MyProfileResponseDto> retrieveMyProfile() {
        MyProfileResponseDto myProfile = myProfileRetrievalService.retrieve();
        return ApiResponse.success(myProfile);
    }
}
