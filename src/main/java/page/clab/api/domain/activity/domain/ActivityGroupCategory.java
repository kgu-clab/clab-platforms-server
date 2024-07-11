package page.clab.api.domain.activity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupCategory {

    STUDY("STUDY", "스터디"),
    PROJECT("PROJECT", "프로젝트");

    private String key;
    private String description;

}
