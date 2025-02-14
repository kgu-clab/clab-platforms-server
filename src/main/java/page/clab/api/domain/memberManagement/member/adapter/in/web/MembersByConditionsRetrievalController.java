package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMembersByConditionsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MembersByConditionsRetrievalController {

    private final RetrieveMembersByConditionsUseCase retrieveMembersByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[A] 멤버 정보 조회(멤버 ID, 이름 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<MemberResponseDto>> retrieveMembersByConditions(
        @RequestParam(name = "id", required = false) String id,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, MemberResponseDto.class);
        PagedResponseDto<MemberResponseDto> members = retrieveMembersByConditionsUseCase.retrieveMembers(id, name,
            pageable);
        return ApiResponse.success(members);
    }
}
