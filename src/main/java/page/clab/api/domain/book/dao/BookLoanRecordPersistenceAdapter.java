package page.clab.api.domain.book.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookLoanRecordPersistenceAdapter implements
        RegisterBookLoanRecordPort,
        RetrieveBookLoanRecordPort {

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Override
    public BookLoanRecord save(BookLoanRecord bookLoanRecord) {
        return bookLoanRecordRepository.save(bookLoanRecord);
    }

    @Override
    public Optional<BookLoanRecord> findById(Long bookLoanRecordId) {
        return bookLoanRecordRepository.findById(bookLoanRecordId);
    }

    @Override
    public BookLoanRecord findByIdOrThrow(Long bookLoanRecordId) {
        return bookLoanRecordRepository.findById(bookLoanRecordId)
                .orElseThrow(() -> new NotFoundException("[BookLoanRecord] id: " + bookLoanRecordId + "에 해당하는 대출 기록이 존재하지 않습니다."));
    }

    @Override
    public Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable) {
        return bookLoanRecordRepository.findByConditions(bookId, borrowerId, status, pageable);
    }

    @Override
    public Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable) {
        return bookLoanRecordRepository.findOverdueBookLoanRecords(pageable);
    }

    @Override
    public Optional<BookLoanRecord> findByBookAndReturnedAtIsNullAndStatus(Book book, BookLoanStatus bookLoanStatus) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNullAndStatus(book, bookLoanStatus);
    }

    @Override
    public BookLoanRecord findByBookAndReturnedAtIsNullAndStatusOrThrow(Book book, BookLoanStatus bookLoanStatus) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNullAndStatus(book, bookLoanStatus)
                .orElseThrow(() -> new NotFoundException("[Book] id: " + book.getId() + "에 해당하는 대출 기록이 존재하지 않습니다."));
    }

    @Override
    public Optional<BookLoanRecord> findByBookAndBorrowerIdAndStatus(Book book, String borrowerId, BookLoanStatus bookLoanStatus) {
        return bookLoanRecordRepository.findByBookAndBorrowerIdAndStatus(book, borrowerId, bookLoanStatus);
    }
}
