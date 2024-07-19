package page.clab.api.domain.activity.review.application.exception;

public class AlreadyReviewedException extends RuntimeException {

    public AlreadyReviewedException(String message) {
        super(message);
    }
}
