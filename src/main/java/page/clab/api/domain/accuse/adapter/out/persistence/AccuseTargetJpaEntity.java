package page.clab.api.domain.accuse.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "accuse_target")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(AccuseTargetId.class)
public class AccuseTargetJpaEntity extends BaseEntity {

    @Id
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Id
    @Column(nullable = false)
    private Long targetReferenceId;

    private Long accuseCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccuseStatus accuseStatus;
}
