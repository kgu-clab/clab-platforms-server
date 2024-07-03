package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.UpdateReviewUseCase;
import page.clab.api.domain.review.application.port.out.LoadReviewPort;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ReviewUpdateService implements UpdateReviewUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final LoadReviewPort loadReviewPort;
    private final RegisterReviewPort registerReviewPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Review review = loadReviewPort.findByIdOrThrow(reviewId);
        review.validateAccessPermission(currentMember);
        review.update(requestDto);
        validationService.checkValid(review);
        return registerReviewPort.save(review).getId();
    }
}
