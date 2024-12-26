package page.clab.api.global.util;

/**
 * {@code LogSanitizerUtil} 클래스는 로그에 기록하기 전 문자열을 안전하게 변환하기 위한 유틸리티 메서드를 제공합니다. 이 클래스는 잠재적으로 해로운 특수 문자를 제거하여 로그의 안전성을
 * 보장합니다.
 */
public class LogSanitizerUtil {

    /**
     * 로그에 기록하기 전에 입력 문자열을 안전하게 변환합니다. 새 줄, 캐리지 리턴 및 기타 특수 문자를 제거하거나 이스케이프 처리합니다.
     *
     * @param input 로그에 기록될 사용자 입력
     * @return 안전한 로그 문자열
     */
    public static String sanitizeForLog(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[\n\r\t]", "_");
    }
}
