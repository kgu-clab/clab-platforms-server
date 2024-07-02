package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.ReviewRemoveUseCase;
import page.clab.api.domain.review.application.port.out.RemoveReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewRemoveService implements ReviewRemoveUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RemoveReviewPort removeReviewPort;

    @Transactional
    @Override
    public Long remove(Long reviewId) throws PermissionDeniedException {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        Review review = removeReviewPort.findByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.delete();
        return removeReviewPort.save(review).getId();
    }
}
