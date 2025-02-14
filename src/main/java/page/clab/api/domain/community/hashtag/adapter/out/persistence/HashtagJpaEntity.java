package page.clab.api.domain.community.hashtag.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.community.hashtag.domain.HashtagCategory;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE hashtag SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "hashtag")
public class HashtagJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "hashtag_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private HashtagCategory hashtagCategory;

    @Column(name = "board_usage", nullable = false)
    private Long boardUsage;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}

