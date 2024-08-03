package page.clab.api.domain.auth.login.application.dto.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginHeader {

    private String secretKey;

    private LoginHeader(String secretKey) {
        this.secretKey = secretKey;
    }

    public static LoginHeader create(String secretKey) {
        return new LoginHeader(secretKey);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }
}
