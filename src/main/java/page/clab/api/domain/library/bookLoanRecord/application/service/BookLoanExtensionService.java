package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.ExtendBookLoanUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class BookLoanExtensionService implements ExtendBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ExternalRetrieveBookUseCase externalRetrieveBookUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    @Override
    public Long extendBookLoan(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = externalRetrieveMemberUseCase.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = externalRetrieveBookUseCase.findByIdOrThrow(requestDto.getBookId());

        book.validateCurrentBorrower(currentMemberId);
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByBookIdAndReturnedAtIsNullAndStatusOrThrow(book.getId(), BookLoanStatus.APPROVED);
        bookLoanRecord.extendLoan(borrowerInfo);

        externalSendNotificationUseCase.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.");

        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}
