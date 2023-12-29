package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.type.dto.ReviewRequestDto;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_group_id")
    private ActivityGroup activityGroup;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull(message = "{notNull.review.content}")
    @Size(min = 1, max = 1000, message = "{size.review.content}")
    private String content;

    @Column(nullable = false)
    private Boolean isPublic;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Review of(ReviewRequestDto reviewRequestDto) {
        return ModelMapperUtil.getModelMapper().map(reviewRequestDto, Review.class);
    }

}
