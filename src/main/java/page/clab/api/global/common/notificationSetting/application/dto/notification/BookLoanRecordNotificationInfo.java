package page.clab.api.global.common.notificationSetting.application.dto.notification;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;

@Getter
@Builder
public class BookLoanRecordNotificationInfo {

    private String memberId;
    private String memberName;
    private String bookTitle;
    private String category;
    private boolean isAvailable;

    public static BookLoanRecordNotificationInfo create(Book book, MemberBorrowerInfoDto borrowerInfo) {
        return BookLoanRecordNotificationInfo.builder()
            .memberId(borrowerInfo.getMemberId())
            .memberName(borrowerInfo.getMemberName())
            .bookTitle(book.getTitle())
            .category(book.getCategory())
            .isAvailable(book.getBorrowerId() == null)
            .build();
    }
}
