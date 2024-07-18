package page.clab.api.domain.memberManagement.member.application.exception;

public class DuplicateMemberIdException extends RuntimeException {

    public DuplicateMemberIdException(String s) {
        super(s);
    }
}
