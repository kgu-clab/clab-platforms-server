package page.clab.api.domain.login.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.LoginService;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginHeader;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.login.dto.response.TwoFactorAuthenticationHeader;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "멤버 로그인", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("")
    public ResponseModel login(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, MemberLockedException, LoginFaliedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        LoginHeader headerData = loginService.login(httpServletRequest, loginRequestDto);
        httpServletResponse.setHeader("X-Clab-Auth", headerData.toJson());
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "TOTP 인증", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("/authenticator")
    public ResponseModel authenticator(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @Valid @RequestBody TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, LoginFaliedException, MemberLockedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        TwoFactorAuthenticationHeader headerData = loginService.authenticator(httpServletRequest, twoFactorAuthenticationRequestDto);
        httpServletResponse.setHeader("X-Clab-Auth", headerData.toJson());
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[S] TOTP 초기화", description = "ROLE_SUPER 권한이 필요함")
    @DeleteMapping("/authenticator/{memberId}")
    @Secured({"ROLE_SUPER"})
    public ResponseModel deleteAuthenticator(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = loginService.resetAuthenticator(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[S] 멤버 토큰 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @DeleteMapping("/revoke/{memberId}")
    @Secured({"ROLE_SUPER"})
    public ResponseModel revoke(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = loginService.revoke(memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 멤버 토큰 재발급", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/reissue")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel reissue(
            HttpServletRequest httpServletRequest
    ) {
        TokenInfo tokenInfo = loginService.reissue(httpServletRequest);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(tokenInfo);
        return responseModel;
    }

}
