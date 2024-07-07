package page.clab.api.domain.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.RetrieveBlogDetailsUseCase;
import page.clab.api.domain.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;

@Service
@RequiredArgsConstructor
public class BlogDetailsRetrievalService implements RetrieveBlogDetailsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBlogPort retrieveBlogPort;

    @Transactional(readOnly = true)
    @Override
    public BlogDetailsResponseDto retrieveBlogDetails(Long blogId) {
        MemberBasicInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberBasicInfo();
        Blog blog = retrieveBlogPort.findByIdOrThrow(blogId);
        boolean isOwner = blog.isOwner(currentMemberInfo.getMemberId());
        return BlogDetailsResponseDto.toDto(blog, currentMemberInfo, isOwner);
    }
}
