package page.clab.api.global.util;

import java.util.regex.Pattern;


public class SwaggerUtil {

    public static final String[] SWAGGER_PATTERNS = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-resources/.*",
            "/swagger-ui.html",
            "/v3/api-docs/.*",
            "/swagger-ui/.*"
    };

    public static boolean isSwaggerRequest(String path) {
        for (String pattern : SWAGGER_PATTERNS) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }

}