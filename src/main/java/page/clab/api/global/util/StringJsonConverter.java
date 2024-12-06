package page.clab.api.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * {@code StringJsonConverter} 클래스는 JPA 엔티티 필드를 JSON 문자열로 변환하거나 JSON 문자열을 리스트로 변환하는 역할을 수행하는 컨버터입니다.
 * <p>
 * 데이터베이스와 애플리케이션 사이에서 {@code List<String>} 타입을 JSON 문자열로 저장 및 변환하는 데 사용됩니다.
 */
@Slf4j
public class StringJsonConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            log.error("Could not convert list to JSON string", e);
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Could not convert JSON string to list", e);
            return null;
        }
    }
}
