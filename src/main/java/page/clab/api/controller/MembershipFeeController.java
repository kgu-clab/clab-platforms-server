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
import org.springframework.web.bind.annotation.*;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.MembershipFeeService;
import page.clab.api.type.dto.MembershipFeeRequestDto;
import page.clab.api.type.dto.MembershipFeeResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee", description = "회비 관련 API")
@Slf4j
public class MembershipFeeController {
    
    private final MembershipFeeService membershipFeeService;

    @Operation(summary = "[U] 회비 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createMembershipFee(
            @Valid @RequestBody MembershipFeeRequestDto MembershipFeeRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        membershipFeeService.createMembershipFee(MembershipFeeRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 회비 정보 조회", description = "ROLE_USER 이상의 권한이 필요함")
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
    @PatchMapping("/{membershipFeeId}")
    public ResponseModel updateMembershipFee(
            @PathVariable Long membershipFeeId,
            @Valid @RequestBody MembershipFeeRequestDto membershipFeeRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        membershipFeeService.updateMembershipFee(membershipFeeId, membershipFeeRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 회비 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{membershipFeeId}")
    public ResponseModel deleteMembershipFee(
            @PathVariable Long membershipFeeId
    ) throws PermissionDeniedException {
        membershipFeeService.deleteMembershipFee(membershipFeeId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }
    
}
