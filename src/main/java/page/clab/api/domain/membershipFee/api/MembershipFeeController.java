package page.clab.api.domain.membershipFee.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
import page.clab.api.domain.membershipFee.application.MembershipFeeService;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee", description = "회비 관련 API")
@Slf4j
public class MembershipFeeController {

    private final MembershipFeeService membershipFeeService;

    @Operation(summary = "[U] 회비 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createMembershipFee(
            @Valid @RequestBody MembershipFeeRequestDto MembershipFeeRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = membershipFeeService.createMembershipFee(MembershipFeeRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 회비 정보 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getMembershipFees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MembershipFeeResponseDto> MembershipFees = membershipFeeService.getMembershipFees(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(MembershipFees);
        return responseModel;
    }

    @Operation(summary = "[U] 회비 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "카테고리를 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel getMembershipFee(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MembershipFeeResponseDto> MembershipFees = membershipFeeService.searchMembershipFee(category, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(MembershipFees);
        return responseModel;
    }

    @Operation(summary = "[A] 회비 정보 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{membershipFeeId}")
    public ResponseModel updateMembershipFee(
            @PathVariable Long membershipFeeId,
            @Valid @RequestBody MembershipFeeRequestDto membershipFeeRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = membershipFeeService.updateMembershipFee(membershipFeeId, membershipFeeRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 회비 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{membershipFeeId}")
    public ResponseModel deleteMembershipFee(
            @PathVariable Long membershipFeeId
    ) throws PermissionDeniedException {
        Long id = membershipFeeService.deleteMembershipFee(membershipFeeId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
