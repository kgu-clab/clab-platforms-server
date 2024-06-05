package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
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
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final MemberService memberService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final ReviewRepository reviewRepository;

    @Transactional
    public Long createReview(ReviewRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(requestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, currentMember);
        Review review = ReviewRequestDto.toEntity(requestDto, currentMember, activityGroup);
        validationService.checkValid(review);
        notifyGroupLeaderOfNewReview(activityGroup, currentMember);
        return reviewRepository.save(review).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ReviewResponseDto> getReviewsByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Review> reviews = reviewRepository.findByConditions(memberId, memberName, activityId, isPublic, pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ReviewResponseDto> getMyReviews(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Review> reviews = getReviewByMember(currentMember, pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ReviewResponseDto> getDeletedReviews(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Review> reviews = reviewRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }

    @Transactional
    public Long updateReview(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Review review = getReviewByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.update(requestDto);
        validationService.checkValid(review);
        return reviewRepository.save(review).getId();
    }

    public Long deleteReview(Long reviewId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Review review = getReviewByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        reviewRepository.delete(review);
        return reviewId;
    }

    private void validateReviewCreationPermission(ActivityGroup activityGroup, Member member) {
        if (!activityGroup.isEnded()) {
            throw new ActivityGroupNotFinishedException("활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다.");
        }
        if (isExistsByMemberAndActivityGroup(member, activityGroup)) {
            throw new AlreadyReviewedException("이미 리뷰를 작성한 활동 그룹입니다.");
        }
    }

    private void notifyGroupLeaderOfNewReview(ActivityGroup activityGroup, Member member) {
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(groupLeader.getMember().getId(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 리뷰를 등록하였습니다.");
        }
    }

    private boolean isExistsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup) {
        return reviewRepository.existsByMemberAndActivityGroup(member, activityGroup);
    }

    public Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("해당 리뷰가 없습니다."));
    }

    private Page<Review> getReviewByMember(Member member, Pageable pageable) {
        return reviewRepository.findAllByMember(member, pageable);
    }

}