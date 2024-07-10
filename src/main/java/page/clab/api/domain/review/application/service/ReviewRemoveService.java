package page.clab.api.domain.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.review.application.port.in.RemoveReviewUseCase;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewRemoveService implements RemoveReviewUseCase {

    private final RetrieveMemberInfoUseCase RetrieveMemberInfoUseCase;
    private final RetrieveReviewPort retrieveReviewPort;
    private final RegisterReviewPort registerReviewPort;

    @Transactional
    @Override
    public Long removeReview(Long reviewId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMember = RetrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Review review = retrieveReviewPort.findByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.delete();
        return registerReviewPort.save(review).getId();
    }
}
