package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.MyReviewsRetrievalUseCase;
import page.clab.api.domain.review.application.port.out.RetrieveMyReviewsPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyReviewsRetrievalService implements MyReviewsRetrievalUseCase {

    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final RetrieveMyReviewsPort retrieveMyReviewsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> retrieve(Pageable pageable) {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        Page<Review> reviews = retrieveMyReviewsPort.findAllByMember(currentMember, pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }
}
