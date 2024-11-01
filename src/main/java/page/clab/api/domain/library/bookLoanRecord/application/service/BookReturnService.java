package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.ReturnBookUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.external.library.book.application.port.ExternalRegisterBookUseCase;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalUpdateMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class BookReturnService implements ReturnBookUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ExternalRegisterBookUseCase externalRegisterBookUseCase;
    private final ExternalRetrieveBookUseCase externalRetrieveBookUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalUpdateMemberUseCase externalUpdateMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    /**
     * 도서 반납을 처리합니다.
     *
     * <p>현재 로그인한 멤버가 대출한 도서를 반납 처리하고,
     * 대출 기록을 "RETURNED"로 표시합니다.
     * 반납 이후 회원의 대출 정지 날짜를 업데이트하고,
     * 반납 완료 알림을 사용자에게 전송합니다.</p>
     *
     * @param requestDto 도서 반납 요청 정보 DTO
     * @return 반납된 대출 기록의 ID
     */
    @Transactional
    @Override
    public Long returnBook(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = externalRetrieveMemberUseCase.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = externalRetrieveBookUseCase.getById(requestDto.getBookId());
        book.returnBook(currentMemberId);
        externalRegisterBookUseCase.save(book);

        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.getByBookIdAndReturnedAtIsNullAndStatus(book.getId(), BookLoanStatus.APPROVED);
        bookLoanRecord.markAsReturned(borrowerInfo);

        externalUpdateMemberUseCase.updateLoanSuspensionDate(borrowerInfo.getMemberId(), borrowerInfo.getLoanSuspensionDate());

        externalSendNotificationUseCase.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 반납이 완료되었습니다.");
        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}
