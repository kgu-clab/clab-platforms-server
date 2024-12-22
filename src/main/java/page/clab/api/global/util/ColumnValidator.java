package page.clab.api.global.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import org.springframework.stereotype.Component;

/**
 * {@code ColumnValidator}는 주어진 클래스에 특정 필드(컬럼)가 존재하는지 검증하는 유틸리티 클래스입니다. 리플렉션을 사용하여 클래스의 모든 필드를 조회하고, 입력된 필드 이름과 일치하는 필드가
 * 있는지 확인합니다.
 *
 * <p>주요 기능:
 * <ul>
 *     <li>{@link #isValidColumn(Class, String)} - 클래스에 특정 필드가 존재하는지 검증</li>
 * </ul>
 */
@Component
public class ColumnValidator {

    public boolean isValidColumn(Class<?> domainClass, String columnName) {
        Field[] fields = domainClass.getDeclaredFields();
        return Arrays.stream(fields)
            .anyMatch(field -> field.getName().equals(columnName));
    }
}
