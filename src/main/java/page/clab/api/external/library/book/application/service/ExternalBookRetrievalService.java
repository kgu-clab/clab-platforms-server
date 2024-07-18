package page.clab.api.external.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBookRetrievalService implements ExternalRetrieveBookUseCase {

    private final RetrieveBookPort retrieveBookPort;

    @Override
    public Book findByIdOrThrow(Long bookId) {
        return retrieveBookPort.findByIdOrThrow(bookId);
    }

    @Override
    public int countByBorrowerId(String borrowerId) {
        return retrieveBookPort.countByBorrowerId(borrowerId);
    }
}
