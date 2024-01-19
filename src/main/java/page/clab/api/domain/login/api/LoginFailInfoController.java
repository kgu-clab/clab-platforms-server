package page.clab.api.domain.login.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.LoginFailInfoService;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/login-fail-info")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인 관련 API")
@Slf4j
public class LoginFailInfoController {

    private final LoginFailInfoService loginFailInfoService;

    @Operation(summary = "[A] 유저 밴 처리", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/ban/{memberId}")
    public ResponseModel banMember(
            @PathVariable(name = "memberId") String memberId
    ) {
        Long id = loginFailInfoService.banMemberById(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 유저 밴 해제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/unban/{memberId}")
    public ResponseModel unbanMember(
            @PathVariable(name = "memberId") String memberId
    ) {
        Long id = loginFailInfoService.unbanMemberById(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
