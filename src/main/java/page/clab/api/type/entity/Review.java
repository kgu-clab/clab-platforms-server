package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.type.dto.ReviewRequestDto;
import page.clab.api.type.dto.ReviewUpdateRequestDto;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isPublic;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Review of(ReviewRequestDto reviewRequestDto) {
        Review review = ModelMapperUtil.getModelMapper().map(reviewRequestDto, Review.class);
        review.setIsPublic(false);
        return review;
    }

    public static Review of(ReviewUpdateRequestDto reviewUpdateRequestDto) {
        return Review.builder()
                .member(Member.builder().id(reviewUpdateRequestDto.getMemberId()).build())
                .content(reviewUpdateRequestDto.getContent())
                .build();
    }

}
