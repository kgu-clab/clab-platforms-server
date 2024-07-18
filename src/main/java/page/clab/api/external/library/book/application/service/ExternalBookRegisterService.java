package page.clab.api.external.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.library.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.external.library.book.application.port.ExternalRegisterBookUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBookRegisterService implements ExternalRegisterBookUseCase {

    private final RegisterBookPort registerBookPort;

    @Override
    public void save(Book book) {
        registerBookPort.save(book);
    }
}
