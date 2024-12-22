package page.clab.api.domain.members.membershipFee.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RetrieveDeletedMembershipFeesUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "Members - Membership Fee", description = "회비")
public class DeletedMembershipFeesRetrievalController {

    private final RetrieveDeletedMembershipFeesUseCase retrieveDeletedMembershipFeesUseCase;

    @Operation(summary = "[S] 삭제된 회비 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("/deleted")
    public ApiResponse<PagedResponseDto<MembershipFeeResponseDto>> retrieveDeletedMembershipFees(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<MembershipFeeResponseDto> membershipFees =
            retrieveDeletedMembershipFeesUseCase.retrieveDeletedMembershipFees(pageable);
        return ApiResponse.success(membershipFees);
    }
}
