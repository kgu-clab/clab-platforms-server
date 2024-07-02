package page.clab.api.domain.blog.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.BlogDetailsRetrievalUseCase;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BlogDetailsRetrievalService implements BlogDetailsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    @Override
    public BlogDetailsResponseDto retrieve(Long blogId) {
        MemberBasicInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberBasicInfo();
        Blog blog = getBlogByIdOrThrow(blogId);
        boolean isOwner = blog.isOwner(currentMemberInfo.getMemberId());
        return BlogDetailsResponseDto.toDto(blog, currentMemberInfo, isOwner);
    }

    private Blog getBlogByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
    }
}