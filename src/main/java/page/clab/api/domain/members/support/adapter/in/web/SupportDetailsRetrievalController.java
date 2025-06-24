package page.clab.api.domain.members.support.adapter.in.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.members.support.application.dto.response.SupportDetailsResponseDto;
import page.clab.api.domain.members.support.application.port.in.RetrieveSupportDetailsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("api/v1/supports")
@RequiredArgsConstructor
@Tag(name = "Members - Support", description = "문의 사항")
public class SupportDetailsRetrievalController {

    private final RetrieveSupportDetailsUseCase retrieveSupportDetailsUseCase;

    @Operation(summary = "[U] 문의 사항 상세 조회", description = "INQUIRY 조회는 ROLE_USER 이상의 권한이 필요함<br>" +
    "모든 게시글의 조회는 ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{supportId}")
    public ApiResponse<SupportDetailsResponseDto> retrieveBlogDetails(
            @PathVariable(name = "supportId") Long supportId
    ) {
        SupportDetailsResponseDto support = retrieveSupportDetailsUseCase.retrieveSupportDetails(supportId);
        return ApiResponse.success(support);
    }


}
