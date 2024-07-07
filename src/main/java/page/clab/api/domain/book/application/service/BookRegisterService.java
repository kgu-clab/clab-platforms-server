package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.RegisterBookUseCase;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookRegisterService implements RegisterBookUseCase {

    private final RegisterBookPort registerBookPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long registerBook(BookRequestDto requestDto) {
        Book book = BookRequestDto.toEntity(requestDto);
        validationService.checkValid(book);
        return registerBookPort.save(book).getId();
    }
}
