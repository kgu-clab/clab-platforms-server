package page.clab.api.domain.memberManagement.member.application.port.in;

public interface EnsureMemberExistenceUseCase {
    void ensureMemberExists(String memberId);
}
