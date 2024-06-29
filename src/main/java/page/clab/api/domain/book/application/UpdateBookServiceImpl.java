package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class UpdateBookServiceImpl implements UpdateBookService {

    private final BookRepository bookRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long execute(Long bookId, BookUpdateRequestDto requestDto) {
        Book book = getBookByIdOrThrow(bookId);
        book.update(requestDto);
        validationService.checkValid(book);
        return bookRepository.save(book).getId();
    }

    private Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }
}
