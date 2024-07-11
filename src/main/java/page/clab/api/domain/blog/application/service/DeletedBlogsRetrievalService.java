package page.clab.api.domain.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.blog.application.port.in.RetrieveDeletedBlogsUseCase;
import page.clab.api.domain.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBlogsRetrievalService implements RetrieveDeletedBlogsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBlogPort retrieveBlogPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogDetailsResponseDto> retrieveDeletedBlogs(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberBasicInfo();
        Page<Blog> blogs = retrieveBlogPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(blogs.map(blog -> BlogDetailsResponseDto.toDto(blog, currentMemberInfo, blog.isOwner(currentMemberInfo.getMemberId()))));
    }
}