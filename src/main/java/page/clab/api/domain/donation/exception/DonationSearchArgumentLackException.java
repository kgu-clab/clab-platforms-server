package page.clab.api.domain.donation.exception;

public class DonationSearchArgumentLackException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "적어도 memberId 또는 name 중 하나를 제공해야 합니다.";

    public DonationSearchArgumentLackException() {
        super(DEFAULT_MESSAGE);
    }

    public DonationSearchArgumentLackException(String s) {
        super(s);
    }

}
