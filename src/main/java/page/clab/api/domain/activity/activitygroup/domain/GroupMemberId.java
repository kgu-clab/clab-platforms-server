package page.clab.api.domain.activity.activitygroup.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class GroupMemberId implements Serializable {

    @EqualsAndHashCode.Include
    private String memberId;

    @EqualsAndHashCode.Include
    private Long activityGroup;
}
