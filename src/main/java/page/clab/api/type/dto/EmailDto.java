package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.util.ModelMapperUtil;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    private List<String> to;

    private String subject;

    private String content;


    public static EmailDto of(List<String> to, String subject, String content) {
        return ModelMapperUtil.getModelMapper().map(EmailDto.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .build(), EmailDto.class);
    }

}
