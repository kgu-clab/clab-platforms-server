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
@Tag(name = "Login")
@Slf4j
public class LoginFailInfoController {

    private final LoginFailInfoService loginFailInfoService;

    @Operation(summary = "유저 밴 처리", description = "유저 밴 처리")
    @PostMapping("/ban/{memberId}")
    public ResponseModel banMember(
            @PathVariable String memberId
    ) throws PermissionDeniedException {
        loginFailInfoService.banMemberById(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 밴 해제", description = "유저 밴 해제")
    @PostMapping("/unban/{memberId}")
    public ResponseModel unbanMember(
            @PathVariable String memberId
    ) throws PermissionDeniedException {
        loginFailInfoService.unbanMemberById(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
