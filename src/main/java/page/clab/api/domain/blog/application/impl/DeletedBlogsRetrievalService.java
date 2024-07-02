package page.clab.api.domain.blog.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.DeletedBlogsRetrievalUseCase;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBlogsRetrievalService implements DeletedBlogsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogDetailsResponseDto> retrieve(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberBasicInfo();
        Page<Blog> blogs = blogRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(blogs.map(blog -> BlogDetailsResponseDto.toDto(blog, currentMemberInfo, blog.isOwner(currentMemberInfo.getMemberId()))));
    }
}