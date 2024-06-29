package page.clab.api.domain.review.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.ReviewUpdateService;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ReviewUpdateServiceImpl implements ReviewUpdateService {

    private final MemberLookupService memberLookupService;
    private final ReviewRepository reviewRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Review review = getReviewByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.update(requestDto);
        validationService.checkValid(review);
        return reviewRepository.save(review).getId();
    }

    private Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("해당 리뷰가 없습니다."));
    }
}
