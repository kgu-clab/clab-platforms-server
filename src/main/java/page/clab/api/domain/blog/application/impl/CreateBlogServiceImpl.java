package page.clab.api.domain.blog.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.CreateBlogService;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.request.BlogRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateBlogServiceImpl implements CreateBlogService {

    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;
    private final BlogRepository blogRepository;

    @Transactional
    @Override
    public Long execute(BlogRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Blog blog = BlogRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(blog);
        return blogRepository.save(blog).getId();
    }
}
