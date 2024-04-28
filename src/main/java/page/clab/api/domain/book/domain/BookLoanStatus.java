package page.clab.api.domain.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookLoanStatus {

    PENDING("PENDING", "대출 신청"),
    APPROVED("APPROVED", "대출 승인"),
    REJECTED("REJECTED", "대출 거절"),
    RETURNED("RETURNED", "반납 완료");

    private String key;
    private String description;

}
