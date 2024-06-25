package page.clab.api.domain.blog.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogEventProcessor implements MemberEventProcessor {

    private final BlogRepository blogRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.registerProcessor(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Blog> blogs = blogRepository.findByMemberId(memberId);
        blogs.forEach(Blog::delete);
        blogRepository.saveAll(blogs);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
