package page.clab.api.global.common.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse<T> {

    @Builder.Default
    private Boolean success = false;

    private T data;

    private String errorMessage;

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public static <T> ErrorResponse<T> failure(Exception e) {
        String exceptionName = e.getClass().getSimpleName();
        return ErrorResponse.<T>builder()
                .errorMessage(exceptionName.toUpperCase())
                .build();
    }

}