package page.clab.api.domain.accuse.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.common.domain.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccuseTarget extends BaseEntity {

    private TargetType targetType;
    private Long targetReferenceId;
    private Long accuseCount;
    private AccuseStatus accuseStatus;

    public void increaseAccuseCount() {
        this.accuseCount++;
    }

    public void updateStatus(AccuseStatus newStatus) {
        this.accuseStatus = newStatus;
    }
}
