package page.clab.api.domain.membershipFee.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee", description = "회비")
@Slf4j
public class MembershipFeeController {

    private final MembershipFeeService membershipFeeService;

    @Operation(summary = "[U] 회비 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createMembershipFee(
            @Valid @RequestBody MembershipFeeRequestDto requestDto
    ) {
        Long id = membershipFeeService.createMembershipFee(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 회비 정보 조회(멤버 ID, 멤버 이름, 카테고리 기준)", description = "ROLE_USER 이상의 권한이 필요함<br> " +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 카테고리 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getMembershipFeesByConditions(
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "memberName", required = false) String memberName,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MembershipFeeResponseDto> membershipFees = membershipFeeService.getMembershipFeesByConditions(memberId, memberName, category, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(membershipFees);
        return responseModel;
    }

    @Operation(summary = "[S] 회비 정보 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{membershipFeeId}")
    public ResponseModel updateMembershipFee(
            @PathVariable(name = "membershipFeeId") Long membershipFeeId,
            @Valid @RequestBody MembershipFeeUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = membershipFeeService.updateMembershipFee(membershipFeeId, requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[S] 회비 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{membershipFeeId}")
    public ResponseModel deleteMembershipFee(
            @PathVariable(name = "membershipFeeId") Long membershipFeeId
    ) throws PermissionDeniedException {
        Long id = membershipFeeService.deleteMembershipFee(membershipFeeId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
