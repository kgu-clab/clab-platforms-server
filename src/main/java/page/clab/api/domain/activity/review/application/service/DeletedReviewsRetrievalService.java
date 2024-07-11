package page.clab.api.domain.activity.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.review.application.dto.response.ReviewResponseDto;
import page.clab.api.domain.activity.review.application.port.in.RetrieveDeletedReviewsUseCase;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedReviewsRetrievalService implements RetrieveDeletedReviewsUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveReviewPort retrieveReviewPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> retrieveDeletedReviews(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Review> reviews = retrieveReviewPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(reviews.map(review -> {
            Member reviewer = retrieveMemberUseCase.findByIdOrThrow(review.getMemberId());
            return ReviewResponseDto.toDto(review, reviewer, review.isOwner(currentMemberId));
        }));
    }
}
