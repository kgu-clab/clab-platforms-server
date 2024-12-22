package page.clab.api.domain.community.hashtag.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.application.port.in.RetrieveHashtagUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/hashtags")
@RequiredArgsConstructor
@Tag(name = "Community - Hashtag", description = "해시태그")
public class HashtagRetrievalController {

    private final RetrieveHashtagUseCase retrieveHashtagUseCase;

    @Operation(summary = "[U] Board에서 사용한 횟수를 포함한 해시태그 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ApiResponse<List<HashtagResponseDto>> retrieveHashtag() {
        List<HashtagResponseDto> hashTags = retrieveHashtagUseCase.retrieveHashtagWithUsedBoardCount();
        return ApiResponse.success(hashTags);
    }
}
