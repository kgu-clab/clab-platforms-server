package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MembersByRecruitmentRegisterService;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
public class MembersByRecruitmentRegisterController {

    private final MembersByRecruitmentRegisterService membersByRecruitmentRegisterService;

    @Operation(summary = "[S] 모집 단위별 합격자 멤버 통합 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PostMapping("/{recruitmentId}")
    public ApiResponse<List<String>> registerMembersByRecruitmentId(
            @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        List<String> ids = membersByRecruitmentRegisterService.register(recruitmentId);
        return ApiResponse.success(ids);
    }

    @Operation(summary = "[S] 모집 단위별 합격자 멤버 개별 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PostMapping("/{recruitmentId}/{memberId}")
    public ApiResponse<String> registerMemberByRecruitmentId(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = membersByRecruitmentRegisterService.register(recruitmentId, memberId);
        return ApiResponse.success(id);
    }
}
