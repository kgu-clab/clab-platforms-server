package page.clab.api.domain.accuse.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(AccuseTargetId.class)
public class AccuseTarget extends BaseEntity {

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

    public void increaseAccuseCount() {
        this.accuseCount++;
    }

    public void updateStatus(AccuseStatus newStatus) {
        this.accuseStatus = newStatus;
    }
}
