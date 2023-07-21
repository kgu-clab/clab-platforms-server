package page.clab.api.type.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ResponseModel {

    @Builder.Default
    private Boolean success = true;

    private Object data;

    public void addData(Object data) {
        this.data = data;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}