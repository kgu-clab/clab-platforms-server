package page.clab.api.domain.community.news.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.community.news.application.dto.request.NewsUpdateRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class News {

    private Long id;
    private String title;
    private String category;
    private String content;
    private String articleUrl;
    private String source;
    private LocalDate date;
    private boolean isDeleted = false;
    private LocalDateTime createdAt;

    public void update(NewsUpdateRequestDto newsUpdateRequestDto) {
        Optional.ofNullable(newsUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(newsUpdateRequestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(newsUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(newsUpdateRequestDto.getArticleUrl()).ifPresent(this::setArticleUrl);
        Optional.ofNullable(newsUpdateRequestDto.getSource()).ifPresent(this::setSource);
        Optional.ofNullable(newsUpdateRequestDto.getDate()).ifPresent(this::setDate);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
