package page.clab.api.global.common.email.exception;

public class MessageSendingFailedException extends RuntimeException {

    public MessageSendingFailedException(String message) {
        super(message);
    }
}

