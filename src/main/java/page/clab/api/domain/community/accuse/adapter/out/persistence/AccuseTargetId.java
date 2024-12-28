package page.clab.api.domain.community.accuse.adapter.out.persistence;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import page.clab.api.domain.community.accuse.domain.TargetType;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class AccuseTargetId implements Serializable {

    @EqualsAndHashCode.Include
    private TargetType targetType;

    @EqualsAndHashCode.Include
    private Long targetReferenceId;

    public static AccuseTargetId create(TargetType targetType, Long targetReferenceId) {
        return new AccuseTargetId(targetType, targetReferenceId);
    }
}
