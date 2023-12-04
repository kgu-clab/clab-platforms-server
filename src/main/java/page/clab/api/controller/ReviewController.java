package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ReviewService;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.ReviewRequestDto;
import page.clab.api.type.dto.ReviewResponseDto;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "[U] 리뷰 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createReview(
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = reviewService.createReview(reviewRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 리뷰 목록", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> reviewResponseDtos = reviewService.getReviews(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 나의 리뷰 목록", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/my-reviews")
    public ResponseModel getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> reviewResponseDtos = reviewService.getMyReviews(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 공개 리뷰 목록", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/public-reviews")
    public ResponseModel getPublicReview(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> reviewResponseDtos = reviewService.getPublicReview(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 리뷰 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "멤버 ID, 이름을 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchReview(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long activityGroupId,
            @RequestParam(required = false) String activityGroupCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> reviewResponseDtos = reviewService.searchReview(memberId, name, activityGroupId, activityGroupCategory, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 리뷰 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/{reviewId}")
    public ResponseModel updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = reviewService.updateReview(reviewId, reviewRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 리뷰 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @DeleteMapping("/{reviewId}")
    public ResponseModel deleteReview(
            @PathVariable Long reviewId
    ) throws PermissionDeniedException {
        Long id = reviewService.deleteReview(reviewId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 리뷰 공개/비공개", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "공개/비공개가 반전됨")
    @PatchMapping("/public/{reviewId}")
    public ResponseModel publicReview(
            @PathVariable Long reviewId
    ) throws PermissionDeniedException {
        Long id = reviewService.publicReview(reviewId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
