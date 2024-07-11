package page.clab.api.domain.members.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScheduleType {

    ALL("ALL", "전체"),
    STUDY("STUDY", "스터디"),
    PROJECT("PROJECT", "프로젝트");

    private String key;
    private String description;

}
