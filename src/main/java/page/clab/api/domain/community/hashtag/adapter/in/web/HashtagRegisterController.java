package page.clab.api.domain.community.hashtag.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.hashtag.application.dto.request.HashtagRequestDto;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.application.port.in.RegisterHashtagUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/hashtags")
@RequiredArgsConstructor
@Tag(name = "Community - Hashtag", description = "해시태그")
public class HashtagRegisterController {

    private final RegisterHashtagUseCase registerHashtagUseCase;

    @Operation(summary = "[A] 해시태그 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<List<HashtagResponseDto>> registerHashtag(
        @Valid @RequestBody List<HashtagRequestDto> requestDto
    ) {
        List<HashtagResponseDto> hashTags = registerHashtagUseCase.registerHashtag(requestDto);
        return ApiResponse.success(hashTags);
    }
}
