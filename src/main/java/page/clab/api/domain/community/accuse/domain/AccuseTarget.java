package page.clab.api.domain.community.accuse.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccuseTarget {

    private TargetType targetType;
    private Long targetReferenceId;
    private Long accuseCount;
    private AccuseStatus accuseStatus;
    private LocalDateTime createdAt;

    public void increaseAccuseCount() {
        this.accuseCount++;
    }

    public void updateStatus(AccuseStatus newStatus) {
        this.accuseStatus = newStatus;
    }
}
