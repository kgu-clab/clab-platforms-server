package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.BlogDetailsRetrievalUseCase;
import page.clab.api.domain.blog.application.port.out.LoadBlogPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;

@Service
@RequiredArgsConstructor
public class BlogDetailsRetrievalService implements BlogDetailsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final LoadBlogPort loadBlogPort;

    @Transactional(readOnly = true)
    @Override
    public BlogDetailsResponseDto retrieve(Long blogId) {
        MemberBasicInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberBasicInfo();
        Blog blog = loadBlogPort.findByIdOrThrow(blogId);
        boolean isOwner = blog.isOwner(currentMemberInfo.getMemberId());
        return BlogDetailsResponseDto.toDto(blog, currentMemberInfo, isOwner);
    }
}
