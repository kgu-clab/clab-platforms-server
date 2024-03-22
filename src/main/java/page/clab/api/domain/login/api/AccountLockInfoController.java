package page.clab.api.domain.login.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.AccountLockInfoService;
import page.clab.api.domain.login.dto.response.AccountLockInfoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/account-lock-info")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인")
@Slf4j
public class AccountLockInfoController {

    private final AccountLockInfoService accountLockInfoService;

    @Operation(summary = "[S] 멤버 밴 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("/ban/{memberId}")
    public ResponseModel banMember(
            HttpServletRequest request,
            @PathVariable(name = "memberId") String memberId
    ) {
        Long id = accountLockInfoService.banMemberById(request, memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[S] 멤버 밴 해제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("/unban/{memberId}")
    public ResponseModel unbanMember(
            HttpServletRequest request,
            @PathVariable(name = "memberId") String memberId
    ) {
        Long id = accountLockInfoService.unbanMemberById(request, memberId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[S] 밴 멤버 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getBanMembers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AccountLockInfoResponseDto> banMembers = accountLockInfoService.getBanMembers(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(banMembers);
        return responseModel;
    }

}
