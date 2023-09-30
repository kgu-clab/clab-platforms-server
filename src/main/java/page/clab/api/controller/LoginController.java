package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.service.LoginService;
import page.clab.api.type.dto.RefreshTokenDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.TokenDto;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.dto.MemberLoginRequestDto;
import page.clab.api.type.etc.Role;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Login")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "유저 로그인", description = "JWT 인증 로그인<br>" +
            "String id;<br>" +
            "String paasword;")
    @PostMapping()
    public ResponseModel login(
            @RequestBody MemberLoginRequestDto memberLoginRequestDto
    ) throws MemberLockedException, LoginFaliedException {
        ResponseModel responseModel = ResponseModel.builder().build();
        String id = memberLoginRequestDto.getId();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = loginService.login(id, password);
        responseModel.addData(tokenInfo);
        return responseModel;
    }

    @Operation(summary = "유저 토큰 재발급", description = "유저 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseModel reissue(
            @RequestBody RefreshTokenDto refreshTokenDto
    ) {
        TokenInfo tokenInfo = loginService.reissue(refreshTokenDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(tokenInfo);
        return responseModel;
    }

    @Operation(summary = "유저 토큰 권한 검사", description = "유저 토큰 권한 검사<br>" +
            "String token;")
    @PostMapping("/role")
    public ResponseModel checkTokenRole(
            @RequestBody TokenDto tokenDto
    ) {
        boolean isAdminRole = loginService.checkTokenRole(tokenDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData((isAdminRole ? Role.ADMIN.getKey() : Role.USER.getKey()));
        return responseModel;
    }

}
