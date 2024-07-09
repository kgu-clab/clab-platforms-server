package page.clab.api.global.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import page.clab.api.global.config.WhitelistPatternsProperties;

import java.util.regex.Pattern;



@Component
public class WhitelistUtil implements InitializingBean {

    private static String[] swaggerPatterns;
    private static String[] actuatorPatterns;
    private static String[] whitelistPatterns;

    @Autowired
    private WhitelistPatternsProperties whitelistPatternsProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        swaggerPatterns = whitelistPatternsProperties.getPatterns().get("api-docs");
        actuatorPatterns = whitelistPatternsProperties.getPatterns().get("actuator");
        whitelistPatterns = whitelistPatternsProperties.getWhitelistPatterns();
    }

    public static boolean isSwaggerRequest(String path) {
        for (String pattern : swaggerPatterns) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActuatorRequest(String path) {
        for (String pattern : actuatorPatterns) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWhitelistRequest(String path) {
        for (String pattern : whitelistPatterns) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }
}