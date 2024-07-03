package page.clab.api.domain.member.application.port.in;

public interface MemberExistenceUseCase {
    void ensureMemberExists(String memberId);
}
