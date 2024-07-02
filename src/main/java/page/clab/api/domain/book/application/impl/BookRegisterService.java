package page.clab.api.domain.book.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.BookRegisterUseCase;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookRegisterService implements BookRegisterUseCase {

    private final BookRepository bookRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long register(BookRequestDto requestDto) {
        Book book = BookRequestDto.toEntity(requestDto);
        validationService.checkValid(book);
        return bookRepository.save(book).getId();
    }
}
