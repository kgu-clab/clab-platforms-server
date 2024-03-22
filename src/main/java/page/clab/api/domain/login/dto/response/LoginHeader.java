package page.clab.api.domain.login.dto.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.auth.domain.ClabAuthResponseStatus;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class LoginHeader {

    private int status;

    private String secretKey;

    private LoginHeader(int status, String secretKey) {
        this.status = status;
        this.secretKey = secretKey;
    }

    public static LoginHeader create(ClabAuthResponseStatus status, String secretKey) {
        return new LoginHeader(status.getHttpStatus(), secretKey);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

}
