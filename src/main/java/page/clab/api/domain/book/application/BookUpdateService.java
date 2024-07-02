package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.BookUpdateUseCase;
import page.clab.api.domain.book.application.port.out.LoadBookPort;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookUpdateService implements BookUpdateUseCase {

    private final LoadBookPort loadBookPort;
    private final RegisterBookPort registerBookPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long bookId, BookUpdateRequestDto requestDto) {
        Book book = loadBookPort.findByIdOrThrow(bookId);
        book.update(requestDto);
        validationService.checkValid(book);
        return registerBookPort.save(book).getId();
    }
}
