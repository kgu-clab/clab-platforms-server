package page.clab.api.type.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class GroupMemberId implements Serializable {

    @EqualsAndHashCode.Include
    private Long user;

    @EqualsAndHashCode.Include
    private Long activityGroup;

}