package page.clab.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.ActivityGroupNotFinishedException;
import page.clab.api.exception.AlreadyReviewedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.ReviewRepository;
import page.clab.api.type.dto.ReviewRequestDto;
import page.clab.api.type.dto.ReviewResponseDto;
import page.clab.api.type.dto.ReviewUpdateRequestDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Review;
import page.clab.api.type.etc.ActivityGroupStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final MemberService memberService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final ReviewRepository reviewRepository;

    public void createReview(ReviewRequestDto reviewRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(reviewRequestDto.getActivityGroupId());
        if (!(activityGroup.getStatus() == ActivityGroupStatus.활동종료)) {
            throw new ActivityGroupNotFinishedException("활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다.");
        }
        if (isExistsByMemberAndActivityGroup(member, activityGroup)) {
            throw new AlreadyReviewedException("이미 리뷰를 작성한 활동 그룹입니다.");
        }
        Review review = Review.of(reviewRequestDto);
        review.setId(null);
        review.setActivityGroup(activityGroup);
        review.setIsPublic(false);
        review.setMember(member);
        reviewRepository.save(review);
  }

    public List<ReviewResponseDto> getReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        return reviews.map(ReviewResponseDto::of).getContent();
    }

    public List<ReviewResponseDto> getMyReviews(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Review> reviews = getReviewByMember(pageable, member);
        return reviews.map(ReviewResponseDto::of).getContent();
    }

    public List<ReviewResponseDto> getPublicReview(Pageable pageable) {
        Page<Review> reviews = getReviewByIsPublic(pageable);
        return reviews.map(ReviewResponseDto::of).getContent();
    }

    public List<ReviewResponseDto> searchReview(String memberId, String name, Long activityGroupId, String activityGroupCategory, Pageable pageable) {
        Page<Review> reviews;
        if (memberId != null) {
            reviews = getReviewByMemberId(memberId, pageable);
        } else if (name != null) {
            reviews = getReviewByMemberName(name, pageable);
        } else if (activityGroupId != null) {
            reviews = getReviewByActivityGroupId(activityGroupId, pageable);
        } else if (activityGroupCategory != null) {
            reviews = getReviewByActivityGroupCategory(activityGroupCategory, pageable);
        } else {
            throw new IllegalArgumentException("적어도 memberId, name 중 하나를 제공해야 합니다.");
        }
        if (reviews.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return reviews.map(ReviewResponseDto::of).getContent();
    }

    public void updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
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

    private boolean isExistsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup) {
        return reviewRepository.existsByMemberAndActivityGroup(member, activityGroup);
    }

    public Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("해당 리뷰가 없습니다."));
    }

    private Page<Review> getReviewByMember(Pageable pageable, Member member) {
        return reviewRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
    }

    private Page<Review> getReviewByIsPublic(Pageable pageable) {
        return reviewRepository.findAllByIsPublicOrderByCreatedAtDesc(true, pageable);
    }

    private Page<Review> getReviewByMemberId(String memberId, Pageable pageable) {
        return reviewRepository.findAllByMember_IdOrderByCreatedAtDesc(memberId, pageable);
    }

    private Page<Review> getReviewByMemberName(String name, Pageable pageable) {
        return reviewRepository.findAllByMember_NameOrderByCreatedAtDesc(name, pageable);
    }

    private Page<Review> getReviewByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return reviewRepository.findAllByActivityGroup_IdOrderByCreatedAtDesc(activityGroupId, pageable);
    }

    private Page<Review> getReviewByActivityGroupCategory(String activityGroupCategory, Pageable pageable) {
        return reviewRepository.findAllByActivityGroup_CategoryOrderByCreatedAtDesc(activityGroupCategory, pageable);
    }

}
