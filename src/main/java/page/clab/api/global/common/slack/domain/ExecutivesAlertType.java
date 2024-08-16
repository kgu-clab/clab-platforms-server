package page.clab.api.global.common.slack.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecutivesAlertType implements AlertType {

    NEW_APPLICATION("새 지원서", "New application has been submitted."),
    NEW_BOARD("새 게시글", "New board has been posted."),
    NEW_MEMBERSHIP_FEE("새 회비 신청", "New membership fee has been submitted.");

    private final String title;
    private final String defaultMessage;
}
