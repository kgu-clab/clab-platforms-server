package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.ReturnBookUseCase;
import page.clab.api.domain.book.application.port.out.LoadBookLoanRecordByBookAndStatusPort;
import page.clab.api.domain.book.application.port.out.LoadBookPort;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.in.UpdateMemberUseCase;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookReturnService implements ReturnBookUseCase {

    private final LoadBookPort loadBookPort;
    private final LoadBookLoanRecordByBookAndStatusPort loadBookLoanRecordByBookAndStatusPort;
    private final RegisterBookPort registerBookPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final UpdateMemberUseCase updateMemberUseCase;
    private final SendNotificationUseCase notificationService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long returnBook(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = retrieveMemberInfoUseCase.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = loadBookPort.findByIdOrThrow(requestDto.getBookId());
        book.returnBook(currentMemberId);
        registerBookPort.save(book);

        BookLoanRecord bookLoanRecord = loadBookLoanRecordByBookAndStatusPort.findByBookAndReturnedAtIsNullAndStatusOrThrow(book, BookLoanStatus.APPROVED);
        bookLoanRecord.markAsReturned(borrowerInfo);
        validationService.checkValid(bookLoanRecord);

        updateMemberUseCase.updateLoanSuspensionDate(borrowerInfo.getMemberId(), borrowerInfo.getLoanSuspensionDate());

        notificationService.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 반납이 완료되었습니다.");
        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}