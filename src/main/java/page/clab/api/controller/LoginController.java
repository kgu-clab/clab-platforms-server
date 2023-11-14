package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.service.LoginService;
import page.clab.api.type.dto.LoginRequestDto;
import page.clab.api.type.dto.RefreshTokenDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.TokenInfo;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인 관련 API")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "유저 로그인", description = "ROLE_ANONYMOUS 이상의 권한이 필요함<br>" +
            "경기대학교 ID, PW로 로그인")
    @PostMapping("")
    public ResponseModel login(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, MemberLockedException, LoginFaliedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        ResponseModel responseModel = ResponseModel.builder().build();
        TokenInfo tokenInfo = loginService.login(httpServletRequest, loginRequestDto);
        responseModel.addData(tokenInfo);
        return responseModel;
    }

    @Operation(summary = "유저 토큰 재발급", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/reissue")
    public ResponseModel reissue(
            HttpServletRequest httpServletRequest,
            @RequestBody RefreshTokenDto refreshTokenDto
    ) {
        TokenInfo tokenInfo = loginService.reissue(httpServletRequest, refreshTokenDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(tokenInfo);
        return responseModel;
    }

}
