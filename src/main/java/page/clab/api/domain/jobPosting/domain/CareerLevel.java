package page.clab.api.domain.jobPosting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CareerLevel {

    FRESHMAN("FRESHMAN", "신입"),
    EXPERIENCED("EXPERIENCED", "경력");

    private final String key;
    private final String description;

}
