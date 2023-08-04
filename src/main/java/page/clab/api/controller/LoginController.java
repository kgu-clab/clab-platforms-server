package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.LoginService;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.dto.UserLoginRequestDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "Login")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "유저 로그인", description = "JWT 인증 로그인")
    @PostMapping("/login")
    public ResponseModel login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        String userId = userLoginRequestDto.getUserId();
        String password = userLoginRequestDto.getPassword();
        TokenInfo tokenInfo = loginService.login(userId, password);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(tokenInfo);
        return responseModel;
    }

    @Operation(summary = "유저 로그인 테스트", description = "유저 로그인 테스트")
    @PostMapping("/login/test")
    public ResponseModel test() {
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
