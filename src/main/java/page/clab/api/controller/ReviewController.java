package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.dto.ReviewRequestDto;
import page.clab.api.type.dto.ReviewResponseDto;
import page.clab.api.type.dto.ReviewUpdateRequestDto;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Review")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "리뷰 등록")
    @PostMapping("")
    public ResponseModel createReview(
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        reviewService.createReview(reviewRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "리뷰 목록", description = "리뷰 목록")
    @GetMapping("")
    public ResponseModel getReviews() {
        List<ReviewResponseDto> reviewResponseDtos = reviewService.getReviews();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "내 리뷰 목록", description = "내 리뷰 목록")
    @GetMapping("/my-reviews")
    public ResponseModel getMyReviews() {
        List<ReviewResponseDto> reviewResponseDtos = reviewService.getMyReviews();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "공개 리뷰 목록", description = "공개 리뷰 목록")
    @GetMapping("/public-reviews")
    public ResponseModel getPublicReview() {
        List<ReviewResponseDto> reviewResponseDtos = reviewService.getPublicReview();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "리뷰 검색", description = "멤버 ID, 이름을 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchReview(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String name
    ) {
        List<ReviewResponseDto> reviewResponseDtos = reviewService.searchReview(memberId, name);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reviewResponseDtos);
        return responseModel;
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 수정")
    @PatchMapping("/{reviewId}")
    public ResponseModel updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        reviewService.updateReview(reviewId, reviewUpdateRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseModel deleteReview(
            @PathVariable Long reviewId
    ) throws PermissionDeniedException {
        reviewService.deleteReview(reviewId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "리뷰 고정/해제", description = "리뷰 고정/해제")
    @PatchMapping("/pin/{reviewId}")
    public ResponseModel pinReview(
            @PathVariable Long reviewId
    ) throws PermissionDeniedException {
        reviewService.publicReview(reviewId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
