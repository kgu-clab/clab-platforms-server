package page.clab.api.domain.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.application.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.library.book.application.port.in.RetrieveDeletedBooksUseCase;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.library.bookLoanRecord.application.port.ExternalRetrieveBookLoanRecordUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeletedBooksRetrievalService implements RetrieveDeletedBooksUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final ExternalRetrieveBookLoanRecordUseCase externalRetrieveBookLoanRecordUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookDetailsResponseDto> retrieveDeletedBooks(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Page<Book> books = retrieveBookPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(books.map((book) -> mapToBookDetailsResponseDto(book, currentMemberInfo.getMemberName())));
    }

    @NotNull
    private BookDetailsResponseDto mapToBookDetailsResponseDto(Book book, String borrowerName) {
        LocalDateTime dueDate = externalRetrieveBookLoanRecordUseCase.getDueDateForBook(book.getId());
        return BookDetailsResponseDto.toDto(book, borrowerName, dueDate);
    }
}
