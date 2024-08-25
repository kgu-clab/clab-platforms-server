package page.clab.api.domain.activity.activitygroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupBoardCategory {

    NOTICE("NOTICE", "공지사항"),
    WEEKLY_ACTIVITY("WEEKLY_ACTIVITY", "주차별활동"),
    FEEDBACK("FEEDBACK", "피드백"),
    ASSIGNMENT("ASSIGNMENT", "과제"),
    SUBMIT("SUBMIT", "제출");

    private final String key;
    private final String description;

    public boolean isNotice() {
        return this == NOTICE;
    }

    public boolean isWeeklyActivity() {
        return this == WEEKLY_ACTIVITY;
    }

    public boolean isFeedback() {
        return this == FEEDBACK;
    }

    public boolean isAssignment() {
        return this == ASSIGNMENT;
    }

    public boolean isSubmit() {
        return this == SUBMIT;
    }
}
