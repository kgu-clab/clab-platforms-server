package page.clab.api.domain.review.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.ReviewRemoveService;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewRemoveServiceImpl implements ReviewRemoveService {

    private final MemberLookupService memberLookupService;
    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public Long remove(Long reviewId) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Review review = getReviewByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.delete();
        return reviewRepository.save(review).getId();
    }

    private Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("해당 리뷰가 없습니다."));
    }
}
