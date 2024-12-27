package page.clab.api.domain.memberManagement.executive.domain;

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
public class Executive {

    private String id;
    private String name;
    private String email;
    private String field;
    private ExecutivePosition position;
    private String imageUrl;
    private Boolean isDeleted;
}
