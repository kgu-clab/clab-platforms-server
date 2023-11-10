package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.MembershipFeeService;
import page.clab.api.type.dto.MembershipFeeRequestDto;
import page.clab.api.type.dto.MembershipFeeResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee")
@Slf4j
public class MembershipFeeController {
    
    private final MembershipFeeService membershipFeeService;

    @Operation(summary = "회비 신청", description = "회비 신청")
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

    @Operation(summary = "회비 정보 조회", description = "회비 정보 조회")
    @GetMapping("")
    public ResponseModel getMembershipFees() {
        List<MembershipFeeResponseDto> MembershipFees = membershipFeeService.getMembershipFees();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(MembershipFees);
        return responseModel;
    }

    @Operation(summary = "회비 검색", description = "카테고리 기반 검색")
    @GetMapping("/search")
    public ResponseModel getMembershipFee(
            @RequestParam String category
    ) {
        List<MembershipFeeResponseDto> MembershipFees = membershipFeeService.searchMembershipFee(category);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(MembershipFees);
        return responseModel;
    }

    @Operation(summary = "회비 정보 수정", description = "회비 정보 수정")
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

    @Operation(summary = "회비 삭제", description = "회비 삭제")
    @DeleteMapping("/{membershipFeeId}")
    public ResponseModel deleteMembershipFee(
            @PathVariable Long membershipFeeId
    ) throws PermissionDeniedException {
        membershipFeeService.deleteMembershipFee(membershipFeeId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }
    
}
