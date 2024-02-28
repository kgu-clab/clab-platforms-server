package page.clab.api.global.util;

import page.clab.api.global.config.SecurityConstants;

import java.util.regex.Pattern;


public class SwaggerUtil {

    public static boolean isSwaggerRequest(String path) {
        for (String pattern : SecurityConstants.SWAGGER_PATTERNS) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }

}