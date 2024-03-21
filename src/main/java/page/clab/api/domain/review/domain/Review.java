package page.clab.api.domain.review.domain;

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
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.dto.request.ReviewRequestDto;
import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.review.content}")
    private String content;

    @Column(nullable = false)
    private Boolean isPublic;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Review create(ReviewRequestDto reviewRequestDto, Member member, ActivityGroup activityGroup) {
        Review review = ModelMapperUtil.getModelMapper().map(reviewRequestDto, Review.class);
        review.setId(null);
        review.setMember(member);
        review.setActivityGroup(activityGroup);
        review.setIsPublic(false);
        return review;
    }

    public void update(ReviewUpdateRequestDto reviewUpdateRequestDto) {
        Optional.ofNullable(reviewUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(reviewUpdateRequestDto.getIsPublic()).ifPresent(this::setIsPublic);
    }

    public boolean isOwner(Member member) {
        return this.member.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 후기를 수정/삭제할 권한이 없습니다.");
        }
    }

}
