package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
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

    private List<MultipartFile> files;


    public static EmailDto of(List<String> to, String subject, String content, List<MultipartFile> files) {
        return ModelMapperUtil.getModelMapper().map(EmailDto.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .files(files)
                .build(), EmailDto.class);
    }

}
