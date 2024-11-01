package page.clab.api.global.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import page.clab.api.global.config.WhitelistPatternsProperties;

import java.util.regex.Pattern;

/**
 * {@code WhitelistPathMatcher} 클래스는 지정된 경로가 화이트리스트에 포함되는지 확인하는 유틸리티 클래스입니다.
 * <p>
 * 이 클래스는 Swagger 및 Actuator와 같은 특정 요청 경로에 대한 화이트리스트 패턴을 관리하고, 주어진 경로가
 * 해당 패턴들과 일치하는지 검사하는 메서드를 제공합니다.
 */
@Component
public class WhitelistPathMatcher implements InitializingBean {

    private static String[] swaggerPatterns;
    private static String[] actuatorPatterns;
    private static String[] whitelistPatterns;

    private final WhitelistPatternsProperties whitelistPatternsProperties;

    public WhitelistPathMatcher(WhitelistPatternsProperties whitelistPatternsProperties) {
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
