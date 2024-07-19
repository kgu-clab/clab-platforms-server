package page.clab.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import page.clab.api.global.util.HtmlCharacterEscapes;

@Configuration
@RequiredArgsConstructor
public class XssProtectConfig {

    private final ObjectMapper mapper;

    @Bean
    public MappingJackson2HttpMessageConverter characterEscapeConverter() {
        ObjectMapper objectMapper = mapper.copy();
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}


