package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.LoginFailInfoService;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/login-fail-info")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인 관련 API")
@Slf4j
public class LoginFailInfoController {

    private final LoginFailInfoService loginFailInfoService;

    @Operation(summary = "[A] 유저 밴 처리", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("/ban/{memberId}")
    public ResponseModel banMember(
            @PathVariable String memberId
    ) throws PermissionDeniedException {
        loginFailInfoService.banMemberById(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 유저 밴 해제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("/unban/{memberId}")
    public ResponseModel unbanMember(
            @PathVariable String memberId
    ) throws PermissionDeniedException {
        loginFailInfoService.unbanMemberById(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
