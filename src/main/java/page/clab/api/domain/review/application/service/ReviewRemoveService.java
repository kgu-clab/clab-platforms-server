package page.clab.api.domain.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.RemoveReviewUseCase;
import page.clab.api.domain.review.application.port.out.LoadReviewPort;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewRemoveService implements RemoveReviewUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final LoadReviewPort loadReviewPort;
    private final RegisterReviewPort registerReviewPort;

    @Transactional
    @Override
    public Long remove(Long reviewId) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Review review = loadReviewPort.findByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.delete();
        return registerReviewPort.save(review).getId();
    }
}
