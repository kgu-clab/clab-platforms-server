package page.clab.api.domain.accuse.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Accuse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccuseStatus accuseStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Accuse of(AccuseRequestDto accuseRequestDto) {
        return ModelMapperUtil.getModelMapper().map(accuseRequestDto, Accuse.class);
    }

    public static Accuse create(AccuseRequestDto accuseRequestDto, Member member) {
        Accuse accuse = ModelMapperUtil.getModelMapper().map(accuseRequestDto, Accuse.class);
        accuse.setId(null);
        accuse.setMember(member);
        accuse.setAccuseStatus(AccuseStatus.PENDING);
        return accuse;
    }

    public void updateReason(String reason) {
        this.reason = reason;
    }

    public void updateStatus(AccuseStatus newStatus) {
        this.accuseStatus = newStatus;
    }

}
