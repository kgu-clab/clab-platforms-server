package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchDeletedBlogsServiceImpl implements FetchDeletedBlogsService {

    private final MemberLookupService memberLookupService;
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogDetailsResponseDto> execute(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberLookupService.getCurrentMemberBasicInfo();
        Page<Blog> blogs = blogRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(blogs.map(blog -> BlogDetailsResponseDto.toDto(blog, currentMemberInfo, blog.isOwner(currentMemberInfo.getMemberId()))));
    }
}