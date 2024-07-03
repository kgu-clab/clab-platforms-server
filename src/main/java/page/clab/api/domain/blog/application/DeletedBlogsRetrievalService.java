package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.DeletedBlogsRetrievalUseCase;
import page.clab.api.domain.blog.application.port.out.RetrieveDeletedBlogsPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBlogsRetrievalService implements DeletedBlogsRetrievalUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RetrieveDeletedBlogsPort retrieveDeletedBlogsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogDetailsResponseDto> retrieve(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberBasicInfo();
        Page<Blog> blogs = retrieveDeletedBlogsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(blogs.map(blog -> BlogDetailsResponseDto.toDto(blog, currentMemberInfo, blog.isOwner(currentMemberInfo.getMemberId()))));
    }
}
