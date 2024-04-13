package page.clab.api.domain.accuse.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
