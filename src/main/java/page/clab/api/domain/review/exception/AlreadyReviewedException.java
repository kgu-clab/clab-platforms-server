package page.clab.api.domain.review.exception;

public class AlreadyReviewedException extends RuntimeException {

    public AlreadyReviewedException(String message) {
        super(message);
    }
}