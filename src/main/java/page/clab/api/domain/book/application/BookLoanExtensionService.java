package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.BookLoanExtensionUseCase;
import page.clab.api.domain.book.application.port.out.LoadBookLoanRecordByBookAndStatusPort;
import page.clab.api.domain.book.application.port.out.LoadBookPort;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanExtensionService implements BookLoanExtensionUseCase {

    private final LoadBookPort loadBookPort;
    private final LoadBookLoanRecordByBookAndStatusPort loadBookLoanRecordByBookAndStatusPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final MemberLookupUseCase memberLookupUseCase;
    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long extend(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = memberLookupUseCase.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = loadBookPort.findByIdOrThrow(requestDto.getBookId());

        book.validateCurrentBorrower(currentMemberId);
        BookLoanRecord bookLoanRecord = loadBookLoanRecordByBookAndStatusPort.findByBookAndReturnedAtIsNullAndStatusOrThrow(book, BookLoanStatus.APPROVED);
        bookLoanRecord.extendLoan(borrowerInfo);
        validationService.checkValid(bookLoanRecord);

        notificationService.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.");

        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}