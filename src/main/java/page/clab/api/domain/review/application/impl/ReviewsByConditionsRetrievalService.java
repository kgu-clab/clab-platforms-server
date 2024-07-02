package page.clab.api.domain.review.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.ReviewsByConditionsRetrievalUseCase;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class ReviewsByConditionsRetrievalService implements ReviewsByConditionsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> retrieve(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        Page<Review> reviews = reviewRepository.findByConditions(memberId, memberName, activityId, isPublic, pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }
}
