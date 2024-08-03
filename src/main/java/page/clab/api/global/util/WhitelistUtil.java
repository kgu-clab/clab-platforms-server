package page.clab.api.global.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import page.clab.api.global.config.WhitelistPatternsProperties;

import java.util.regex.Pattern;

@Component
public class WhitelistUtil implements InitializingBean {

    private static String[] swaggerPatterns;
    private static String[] actuatorPatterns;
    private static String[] whitelistPatterns;

    private final WhitelistPatternsProperties whitelistPatternsProperties;

    public WhitelistUtil(WhitelistPatternsProperties whitelistPatternsProperties) {
        this.whitelistPatternsProperties = whitelistPatternsProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        swaggerPatterns = whitelistPatternsProperties.getApiDocs();
        actuatorPatterns = whitelistPatternsProperties.getActuator();
        whitelistPatterns = whitelistPatternsProperties.getWhitelistPatterns();
    }

    public static boolean isSwaggerRequest(String path) {
        return isPatternMatch(path, swaggerPatterns);
    }

    public static boolean isActuatorRequest(String path) {
        return isPatternMatch(path, actuatorPatterns);
    }

    public static boolean isWhitelistRequest(String path) {
        return isPatternMatch(path, whitelistPatterns);
    }

    public static boolean isSwaggerIndexEndpoint(String path) {
        return swaggerPatterns[2].equals(path);
    }

    private static boolean isPatternMatch(String path, String[] patterns) {
        for (String pattern : patterns) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }
}
