package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.activityGroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    private String applyReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static ApplyForm of(ApplyFormRequestDto requestDto){
        return ApplyForm.builder()
                .applyReason(requestDto.getApplyReason())
                .build();
    }

}
