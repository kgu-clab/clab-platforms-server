package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.SharedAccountService;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.SharedAccountRequestDto;
import page.clab.api.type.dto.SharedAccountResponseDto;

@RestController
@RequestMapping("/shared-accounts")
@RequiredArgsConstructor
@Tag(name = "SharedAccount", description = "공동계정 관련 API")
@Slf4j
public class SharedAccountController {

    private final SharedAccountService sharedAccountService;

    @Operation(summary = "[A] 공동계정 추가", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createSharedAccount(
            @Valid @RequestBody SharedAccountRequestDto sharedAccountRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        sharedAccountService.createSharedAccount(sharedAccountRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 공동계정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getSharedAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<SharedAccountResponseDto> sharedAccounts = sharedAccountService.getSharedAccounts(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(sharedAccounts);
        return responseModel;
    }

    @Operation(summary = "[A] 공동계정 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("/{accountId}")
    public ResponseModel updateSharedAccount(
            @PathVariable("accountId") Long accountId,
            @Valid @RequestBody SharedAccountRequestDto sharedAccountRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        sharedAccountService.updateSharedAccount(accountId, sharedAccountRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 공동계정 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{accountId}")
    public ResponseModel deleteSharedAccount(
            @PathVariable("accountId") Long accountId
    ) throws PermissionDeniedException {
        sharedAccountService.deleteSharedAccount(accountId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}

