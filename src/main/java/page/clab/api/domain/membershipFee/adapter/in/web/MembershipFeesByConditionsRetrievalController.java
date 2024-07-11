package page.clab.api.domain.membershipFee.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.membershipFee.application.port.in.RetrieveMembershipFeesByConditionsUseCase;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee", description = "회비")
public class MembershipFeesByConditionsRetrievalController {

    private final RetrieveMembershipFeesByConditionsUseCase retrieveMembershipFeesByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 회비 정보 조회(멤버 ID, 멤버 이름, 카테고리, 상태 기준)", description = "ROLE_USER 이상의 권한이 필요함<br> " +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 카테고리 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "계좌 정보는 관리자 이상의 권한만 조회 가능<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, amount, memberId")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("")
    public ApiResponse<PagedResponseDto<MembershipFeeResponseDto>> retrieveMembershipFeesByConditions(
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "memberName", required = false) String memberName,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "status", required = false) MembershipFeeStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, MembershipFeeResponseDto.class);
        PagedResponseDto<MembershipFeeResponseDto> membershipFees =
                retrieveMembershipFeesByConditionsUseCase.retrieveMembershipFees(memberId, memberName, category, status, pageable);
        return ApiResponse.success(membershipFees);
    }
}
