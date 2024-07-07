package page.clab.api.domain.member.application.port.in;

public interface EnsureMemberExistenceUseCase {
    void ensureMemberExists(String memberId);
}
