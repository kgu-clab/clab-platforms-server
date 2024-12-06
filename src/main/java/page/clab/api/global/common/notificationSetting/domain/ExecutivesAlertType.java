package page.clab.api.global.common.notificationSetting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecutivesAlertType implements AlertType {

    NEW_APPLICATION("새 지원서", "New application has been submitted.", AlertCategory.EXECUTIVES),
    NEW_BOARD("새 게시글", "New board has been posted.", AlertCategory.EXECUTIVES),
    NEW_MEMBERSHIP_FEE("새 회비 신청", "New membership fee has been submitted.", AlertCategory.EXECUTIVES),
    NEW_BOOK_LOAN_REQUEST("도서 대출 신청", "New book loan request has been submitted.", AlertCategory.EXECUTIVES);

    private final String title;
    private final String defaultMessage;
    private final AlertCategory category;
}
