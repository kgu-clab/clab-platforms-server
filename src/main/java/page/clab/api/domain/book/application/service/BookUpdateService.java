package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.UpdateBookUseCase;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookUpdateService implements UpdateBookUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final RegisterBookPort registerBookPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long updateBookInfo(Long bookId, BookUpdateRequestDto requestDto) {
        Book book = retrieveBookPort.findByIdOrThrow(bookId);
        book.update(requestDto);
        validationService.checkValid(book);
        return registerBookPort.save(book).getId();
    }
}
