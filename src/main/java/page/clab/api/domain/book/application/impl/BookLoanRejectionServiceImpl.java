package page.clab.api.domain.book.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.BookLoanRejectionService;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanRejectionServiceImpl implements BookLoanRejectionService {

    private final BookLoanRecordRepository bookLoanRecordRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long reject(Long bookLoanRecordId) {
        BookLoanRecord bookLoanRecord = getBookLoanRecordByIdOrThrow(bookLoanRecordId);
        bookLoanRecord.reject();
        validationService.checkValid(bookLoanRecord);
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    private BookLoanRecord getBookLoanRecordByIdOrThrow(Long bookLoanRecordId) {
        return bookLoanRecordRepository.findById(bookLoanRecordId)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }
}
