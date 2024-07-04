package page.clab.api.domain.member.application.port.out;

public interface CheckMemberExistencePort {
    boolean existsById(String memberId);

    boolean existsByContact(String contact);

    boolean existsByEmail(String email);
}
