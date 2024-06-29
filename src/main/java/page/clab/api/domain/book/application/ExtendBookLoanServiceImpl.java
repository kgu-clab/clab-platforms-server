package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ExtendBookLoanServiceImpl implements ExtendBookLoanService {

    private final BookRepository bookRepository;
    private final BookLoanRecordRepository bookLoanRecordRepository;
    private final MemberLookupService memberLookupService;
    private final NotificationService notificationService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long execute(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = memberLookupService.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = getBookByIdOrThrow(requestDto.getBookId());

        book.validateCurrentBorrower(currentMemberId);
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        bookLoanRecord.extendLoan(borrowerInfo);
        validationService.checkValid(bookLoanRecord);

        notificationService.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.");

        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    private Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }

    private BookLoanRecord getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(Book book) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNullAndStatus(book, BookLoanStatus.APPROVED)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }
}