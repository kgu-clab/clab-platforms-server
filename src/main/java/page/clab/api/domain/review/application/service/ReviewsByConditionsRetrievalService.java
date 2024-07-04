package page.clab.api.domain.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.RetrieveReviewsByConditionsUseCase;
import page.clab.api.domain.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class ReviewsByConditionsRetrievalService implements RetrieveReviewsByConditionsUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveReviewPort retrieveReviewPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> retrieveReviews(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Page<Review> reviews = retrieveReviewPort.findByConditions(memberId, memberName, activityId, isPublic, pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }
}
