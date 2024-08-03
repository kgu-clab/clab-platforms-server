package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberBirthdayResponseDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberBirthdaysThisMonthUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberBirthdayRetrievalThisMonthController {

    private final RetrieveMemberBirthdaysThisMonthUseCase retrieveMemberBirthdaysThisMonthUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "이달의 생일자 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @GetMapping("/birthday")
    public ApiResponse<PagedResponseDto<MemberBirthdayResponseDto>> retrieveBirthdaysThisMonth(
            @RequestParam(name = "month") int month,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "birth") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, MemberBirthdayResponseDto.class);
        PagedResponseDto<MemberBirthdayResponseDto> birthdayMembers =
                retrieveMemberBirthdaysThisMonthUseCase.retrieveBirthdaysThisMonth(month, pageable);
        return ApiResponse.success(birthdayMembers);
    }
}
