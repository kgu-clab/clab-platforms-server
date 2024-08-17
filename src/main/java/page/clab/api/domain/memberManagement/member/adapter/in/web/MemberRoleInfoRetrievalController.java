package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberRoleInfoResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberRoleInfoUseCase;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberRoleInfoRetrievalController {

    private final RetrieveMemberRoleInfoUseCase retrieveMemberRoleInfoUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[A] 멤버 권한 조회", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 권한 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    public ApiResponse<List<MemberRoleInfoResponseDto>> retrieveMemberRoleInfo(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) Role role,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws InvalidColumnException, SortingArgumentException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, MemberRoleInfoResponseDto.class);
        List<MemberRoleInfoResponseDto> memberRoles = retrieveMemberRoleInfoUseCase.retrieveMemberRoleInfo(memberId, memberName, role, pageable);
        return ApiResponse.success(memberRoles);
    }
}
