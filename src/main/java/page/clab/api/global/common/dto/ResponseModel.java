package page.clab.api.global.common.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseModel<T> {

    @Builder.Default
    private Boolean success = true;

    private T data;

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public static <T> ResponseModel<T> success() {
        return ResponseModel.<T>builder().build();
    }

    public static <T> ResponseModel<T> success(T data) {
        return ResponseModel.<T>builder()
                .data(data)
                .build();
    }

    public static <T> ResponseModel<T> failure() {
        return ResponseModel.<T>builder()
                .success(false)
                .build();
    }

    public static <T> ResponseModel<T> failure(T data) {
        return ResponseModel.<T>builder()
                .success(false)
                .data(data)
                .build();
    }

}