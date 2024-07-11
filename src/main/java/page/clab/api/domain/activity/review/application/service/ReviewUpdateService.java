package page.clab.api.domain.activity.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.review.application.dto.request.ReviewUpdateRequestDto;
import page.clab.api.domain.activity.review.application.port.in.UpdateReviewUseCase;
import page.clab.api.domain.activity.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class ReviewUpdateService implements UpdateReviewUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveReviewPort retrieveReviewPort;
    private final RegisterReviewPort registerReviewPort;

    @Transactional
    @Override
    public Long updateReview(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMember = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Review review = retrieveReviewPort.findByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.update(requestDto);
        return registerReviewPort.save(review).getId();
    }
}
