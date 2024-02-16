package page.clab.api.domain.activityGroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupBoardCategory {

    NOTICE("NOTICE", "공지사항"),
    WEEKLY_ACTIVITY("WEEKLY_ACTIVITY", "주차별활동"),
    FEEDBACK("FEEDBACK", "피드백"),
    ASSIGNMENT("ASSIGNMENT", "과제제출");

    private String key;
    private String description;

}