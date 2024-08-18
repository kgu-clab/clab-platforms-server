package page.clab.api.domain.community.news.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Boolean isDeleted;
    private LocalDateTime createdAt;
}
