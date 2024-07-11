package page.clab.api.domain.community.news.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.URL;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE news SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "news", indexes = { @Index(name = "idx_article_url", columnList = "articleUrl") })
public class NewsJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.news.title}")
    private String title;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.news.category}")
    private String category;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.news.content}")
    private String content;

    @Column(nullable = false)
    @URL(message = "{url.news.articleUrl}")
    private String articleUrl;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private LocalDate date;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
