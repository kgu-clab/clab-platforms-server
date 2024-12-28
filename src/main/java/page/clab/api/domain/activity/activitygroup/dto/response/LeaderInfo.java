package page.clab.api.domain.activity.activitygroup.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeaderInfo {

    private String id;
    private String name;

    @JsonIgnore
    private LocalDateTime createdAt;
}
