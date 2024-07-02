package page.clab.api.domain.book.application.port.in;

public interface BookLoanApprovalUseCase {
    Long approve(Long bookLoanRecordId);
}
