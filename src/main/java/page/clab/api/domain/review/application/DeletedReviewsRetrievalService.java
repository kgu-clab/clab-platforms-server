package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.in.DeletedReviewsRetrievalUseCase;
import page.clab.api.domain.review.application.port.out.RetrieveDeletedReviewsPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedReviewsRetrievalService implements DeletedReviewsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveDeletedReviewsPort retrieveDeletedReviewsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> retrieve(Pageable pageable) {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        Page<Review> reviews = retrieveDeletedReviewsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }
}
