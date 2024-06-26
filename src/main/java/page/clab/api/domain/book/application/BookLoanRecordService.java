package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.book.exception.BookAlreadyAppliedForLoanException;
import page.clab.api.domain.book.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanRecordService {

    private final BookService bookService;

    private final MemberLookupService memberLookupService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final BookRepository bookRepository;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    public Long requestBookLoan(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException {
        try {
            MemberBorrowerInfoDto borrowerInfo = memberLookupService.getCurrentMemberBorrowerInfo();

            borrowerInfo.checkLoanSuspension();
            validateBorrowLimit(borrowerInfo.getMemberId());

            Book book = bookService.getBookByIdOrThrow(requestDto.getBookId());
            checkIfLoanAlreadyApplied(book, borrowerInfo.getMemberId());

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book, borrowerInfo);
            validationService.checkValid(bookLoanRecord);

            notificationService.sendNotificationToMember(borrowerInfo.getMemberId(), "[" + book.getTitle() + "] 도서 대출 신청이 완료되었습니다.");
            return bookLoanRecordRepository.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출 신청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    @Transactional
    public Long returnBook(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = memberLookupService.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = bookService.getBookByIdOrThrow(requestDto.getBookId());
        book.returnBook(currentMemberId);
        bookRepository.save(book);

        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        bookLoanRecord.markAsReturned(borrowerInfo);
        validationService.checkValid(bookLoanRecord);

        memberLookupService.updateLoanSuspensionDate(borrowerInfo.getMemberId(), borrowerInfo.getLoanSuspensionDate());

        notificationService.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 반납이 완료되었습니다.");
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    @Transactional
    public Long extendBookLoan(BookLoanRecordRequestDto requestDto) {
        MemberBorrowerInfoDto borrowerInfo = memberLookupService.getCurrentMemberBorrowerInfo();
        String currentMemberId = borrowerInfo.getMemberId();
        Book book = bookService.getBookByIdOrThrow(requestDto.getBookId());

        book.validateCurrentBorrower(currentMemberId);
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        bookLoanRecord.extendLoan(borrowerInfo);
        validationService.checkValid(bookLoanRecord);

        notificationService.sendNotificationToMember(currentMemberId, "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.");

        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    @Transactional
    public Long approveBookLoan(Long bookLoanRecordId) {
        String borrowerId = memberLookupService.getCurrentMemberId();
        BookLoanRecord bookLoanRecord = getBookLoanRecordByIdOrThrow(bookLoanRecordId);
        Book book = bookService.getBookByIdOrThrow(bookLoanRecord.getBook().getId());

        book.validateBookIsNotBorrowed();
        validateBorrowLimit(borrowerId);
        bookLoanRecord.approve();

        validationService.checkValid(bookLoanRecord);
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    @Transactional
    public Long rejectBookLoan(Long bookLoanRecordId) {
        BookLoanRecord bookLoanRecord = getBookLoanRecordByIdOrThrow(bookLoanRecordId);
        bookLoanRecord.reject();
        validationService.checkValid(bookLoanRecord);
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    public BookLoanRecord getBookLoanRecordByIdOrThrow(Long bookLoanRecordId) {
        return bookLoanRecordRepository.findById(bookLoanRecordId)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BookLoanRecordResponseDto> getBookLoanRecordsByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable) {
        Page<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordRepository.findByConditions(bookId, borrowerId, status, pageable);
        return new PagedResponseDto<>(bookLoanRecords);
    }

    public PagedResponseDto<BookLoanRecordOverdueResponseDto> getOverdueBookLoanRecords(Pageable pageable) {
        Page<BookLoanRecordOverdueResponseDto> overdueBookLoanRecords = bookLoanRecordRepository.findOverdueBookLoanRecords(pageable);
        return new PagedResponseDto<>(overdueBookLoanRecords);
    }

    public BookLoanRecord getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(Book book) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNullAndStatus(book, BookLoanStatus.APPROVED)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = bookService.getNumberOfBooksBorrowedByMember(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }

    private void checkIfLoanAlreadyApplied(Book book, String borrowerId) {
        bookLoanRecordRepository.findByBookAndBorrowerIdAndStatus(book, borrowerId, BookLoanStatus.PENDING)
                .ifPresent(bookLoanRecord -> {
                    throw new BookAlreadyAppliedForLoanException("이미 대출 신청한 도서입니다.");
                });
    }

}