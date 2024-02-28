package page.clab.api.global.common.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Status {
    CLAB_ATUH(HttpStatus.OK, "1" + HttpServletResponse.SC_OK),

    ;
    private final HttpStatus httpStatus;
    private final String customHttpStatus;

}
