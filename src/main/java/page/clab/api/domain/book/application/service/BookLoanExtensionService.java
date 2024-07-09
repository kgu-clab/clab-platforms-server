package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.application.port.in.ExtendBookLoanUseCase;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanExtensionService implements ExtendBookLoanUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final SendNotificationUseCase notificationService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long extendBookLoan(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = retrieveMemberInfoUseCase.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = retrieveBookPort.findByIdOrThrow(requestDto.getBookId());

        book.validateCurrentBorrower(currentMemberId);
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByBookAndReturnedAtIsNullAndStatusOrThrow(book, BookLoanStatus.APPROVED);
        bookLoanRecord.extendLoan(borrowerInfo);
        validationService.checkValid(bookLoanRecord);

        notificationService.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.");

        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}
