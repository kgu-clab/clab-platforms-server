package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.ReviewRemoveUseCase;
import page.clab.api.domain.review.application.port.out.LoadReviewPort;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewRemoveService implements ReviewRemoveUseCase {

    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final LoadReviewPort loadReviewPort;
    private final RegisterReviewPort registerReviewPort;

    @Transactional
    @Override
    public Long remove(Long reviewId) throws PermissionDeniedException {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        Review review = loadReviewPort.findByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.delete();
        return registerReviewPort.save(review).getId();
    }
}
