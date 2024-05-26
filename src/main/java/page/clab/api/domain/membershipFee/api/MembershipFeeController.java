package page.clab.api.domain.membershipFee.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.membershipFee.application.MembershipFeeService;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

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
    public ApiResponse<Long> createMembershipFee(
            @Valid @RequestBody MembershipFeeRequestDto requestDto
    ) {
        Long id = membershipFeeService.createMembershipFee(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 회비 정보 조회(멤버 ID, 멤버 이름, 카테고리, 상태 기준)", description = "ROLE_USER 이상의 권한이 필요함<br> " +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 카테고리 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "계좌 정보는 관리자 이상의 권한만 조회 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<MembershipFeeResponseDto>> getMembershipFeesByConditions(
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "memberName", required = false) String memberName,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "status", required = false) MembershipFeeStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, MembershipFee.class);
        PagedResponseDto<MembershipFeeResponseDto> membershipFees = membershipFeeService.getMembershipFeesByConditions(memberId, memberName, category, status, pageable);
        return ApiResponse.success(membershipFees);
    }

    @Operation(summary = "[S] 회비 정보 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{membershipFeeId}")
    public ApiResponse<Long> updateMembershipFee(
            @PathVariable(name = "membershipFeeId") Long membershipFeeId,
            @Valid @RequestBody MembershipFeeUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = membershipFeeService.updateMembershipFee(membershipFeeId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[S] 회비 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{membershipFeeId}")
    public ApiResponse<Long> deleteMembershipFee(
            @PathVariable(name = "membershipFeeId") Long membershipFeeId
    ) throws PermissionDeniedException {
        Long id = membershipFeeService.deleteMembershipFee(membershipFeeId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 회비 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<MembershipFeeResponseDto>> getDeletedMembershipFees(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MembershipFeeResponseDto> membershipFees = membershipFeeService.getDeletedMembershipFees(pageable);
        return ApiResponse.success(membershipFees);
    }

}
