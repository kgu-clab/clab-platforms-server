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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accuse extends BaseEntity {

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

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.accuse.reason}")
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccuseStatus accuseStatus;

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
