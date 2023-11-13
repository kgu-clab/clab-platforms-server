package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.ReviewRepository;
import page.clab.api.type.dto.ReviewRequestDto;
import page.clab.api.type.dto.ReviewResponseDto;
import page.clab.api.type.dto.ReviewUpdateRequestDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final MemberService memberService;

    private final ReviewRepository reviewRepository;

    public void createReview(ReviewRequestDto reviewRequestDto) {
        Member member = memberService.getCurrentMember();
        Review review = Review.of(reviewRequestDto);
        review.setMember(member);
        reviewRepository.save(review);
    }

    public List<ReviewResponseDto> getReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(ReviewResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getMyReviews() {
        Member member = memberService.getCurrentMember();
        List<Review> reviews = reviewRepository.findAllByMember(member);
        return reviews.stream()
                .map(ReviewResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getPublicReview() {
        List<Review> reviews = reviewRepository.findAllByIsPublic(true);
        return reviews.stream()
                .map(ReviewResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> searchReview(String memberId, String name) {
        List<Review> reviews = new ArrayList<>();
        if (memberId != null) {
            reviews.addAll(reviewRepository.findAllByMember_Id(memberId));
        } else if (name != null) {
            reviews.addAll(reviewRepository.findAllByMember_Name(name));
        } else {
            throw new IllegalArgumentException("적어도 memberId, name 중 하나를 제공해야 합니다.");
        }
        if (reviews.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return reviews.stream()
                .map(ReviewResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        log.info("member.getId(): {}", member.getId());
        log.info("reviewUpdateRequestDto.getMemberId(): {}", reviewUpdateRequestDto.getMemberId());
        if (!member.getId().equals(reviewUpdateRequestDto.getMemberId())) {
            throw new PermissionDeniedException("해당 리뷰를 수정할 권한이 없습니다.");
        }
        Review review = getReviewByIdOrThrow(reviewId);
        Review updatedReview = Review.of(reviewUpdateRequestDto);
        updatedReview.setId(review.getId());
        updatedReview.setIsPublic(review.getIsPublic());
        updatedReview.setCreatedAt(review.getCreatedAt());
        reviewRepository.save(updatedReview);
    }

    public void deleteReview(Long reviewId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!(member.getId().equals(reviewId) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 리뷰를 삭제할 권한이 없습니다.");
        }
        Review review = getReviewByIdOrThrow(reviewId);
        reviewRepository.delete(review);
    }

    public void publicReview(Long reviewId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("해당 리뷰를 고정할 권한이 없습니다.");
        }
        Review review = getReviewByIdOrThrow(reviewId);
        review.setIsPublic(!review.getIsPublic());
        reviewRepository.save(review);
    }

    public Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("해당 리뷰가 없습니다."));
    }

}
