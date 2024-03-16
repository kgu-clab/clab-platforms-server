package page.clab.api.domain.review.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotFinishedException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.request.ReviewRequestDto;
import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.domain.review.exception.AlreadyReviewedException;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final MemberService memberService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final NotificationService notificationService;

    private final ReviewRepository reviewRepository;

    @Transactional
    public Long createReview(ReviewRequestDto reviewRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(reviewRequestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, member);
        Review review = Review.of(reviewRequestDto, member, activityGroup);
        review.setId(null);
        Long id = reviewRepository.save(review).getId();
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(
                    groupLeader.getMember().getId(),
                    "[" + activityGroup.getName() + "] " + member.getName() + "님이 리뷰를 등록하였습니다."
            );
        }
        return id;
    }

    public PagedResponseDto<ReviewResponseDto> getMyReviews(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Review> reviews = getReviewByMember(pageable, member);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.of(review, member.getId())));
    }

    public PagedResponseDto<ReviewResponseDto> getReviewsByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Review> page = reviewRepository.findReviewsByConditions(memberId, memberName, activityId, isPublic, pageable);
        List<ReviewResponseDto> reviewResponseDtos = page.getContent().stream()
                .map(review -> ReviewResponseDto.of(review, member.getId()))
                .toList();
        return new PagedResponseDto<>(reviewResponseDtos, pageable, reviewResponseDtos.size());
    }

    public Long updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Review review = getReviewByIdOrThrow(reviewId);
        if (!(member.getId().equals(review.getMember().getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 리뷰를 수정할 권한이 없습니다.");
        }
        review.update(reviewUpdateRequestDto);
        return reviewRepository.save(review).getId();
    }

    public Long deleteReview(Long reviewId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Review review = getReviewByIdOrThrow(reviewId);
        if (!(member.getId().equals(review.getMember().getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 리뷰를 삭제할 권한이 없습니다.");
        }
        reviewRepository.delete(review);
        return review.getId();
    }

    private void validateReviewCreationPermission(ActivityGroup activityGroup, Member member) {
        if (!(activityGroup.getStatus() == ActivityGroupStatus.END)) {
            throw new ActivityGroupNotFinishedException("활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다.");
        }
        if (isExistsByMemberAndActivityGroup(member, activityGroup)) {
            throw new AlreadyReviewedException("이미 리뷰를 작성한 활동 그룹입니다.");
        }
    }

    private boolean isExistsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup) {
        return reviewRepository.existsByMemberAndActivityGroup(member, activityGroup);
    }

    public boolean isReviewExistsById(Long id) {
        return reviewRepository.existsById(id);
    }

    public Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("해당 리뷰가 없습니다."));
    }

    private Page<Review> getReviewByMember(Pageable pageable, Member member) {
        return reviewRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
    }

}