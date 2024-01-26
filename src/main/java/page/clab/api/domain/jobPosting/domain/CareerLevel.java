package page.clab.api.domain.jobPosting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CareerLevel {

    FRESHMAN("FRESHMAN", "신입"),
    EXPERIENCED("EXPERIENCED", "경력"),
    NOT_SPECIFIED("NOT_SPECIFIED", "무관");

    private final String key;
    private final String description;

}
