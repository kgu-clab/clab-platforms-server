package page.clab.api.domain.blog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.blog.dto.request.BlogRequestDto;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false, length = 10000)
    @Size(min = 1, max = 10000, message = "{size.blog.content}")
    private String content;

    private String imageUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Blog of(BlogRequestDto blogRequestDto) {
        return ModelMapperUtil.getModelMapper().map(blogRequestDto, Blog.class);
    }

    public void update(BlogUpdateRequestDto blogUpdateRequestDto) {
        Optional.ofNullable(blogUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(blogUpdateRequestDto.getSubTitle()).ifPresent(this::setSubTitle);
        Optional.ofNullable(blogUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(blogUpdateRequestDto.getImageUrl()).ifPresent(this::setImageUrl);
    }

}
