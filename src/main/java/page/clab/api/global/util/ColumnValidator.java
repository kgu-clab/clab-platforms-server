package page.clab.api.global.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
public class ColumnValidator {

    public boolean isValidColumn(Class<?> domainClass, String columnName) {
        Field[] fields = domainClass.getDeclaredFields();
        return Arrays.stream(fields)
                .anyMatch(field -> field.getName().equals(columnName));
    }
}
