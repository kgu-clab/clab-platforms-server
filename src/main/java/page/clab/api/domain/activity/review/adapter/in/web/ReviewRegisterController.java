package page.clab.api.domain.activity.review.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.review.application.dto.request.ReviewRequestDto;
import page.clab.api.domain.activity.review.application.port.in.RegisterReviewUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Activity - Review", description = "활동 리뷰")
public class ReviewRegisterController {

    private final RegisterReviewUseCase registerReviewUseCase;

    @Operation(summary = "[U] 리뷰 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerReview(
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        Long id = registerReviewUseCase.registerReview(requestDto);
        return ApiResponse.success(id);
    }
}
