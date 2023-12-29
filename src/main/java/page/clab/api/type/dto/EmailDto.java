package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    @Schema(description = "받는 사람 이메일 리스트", example = "[\"" + "clab.coreteam@gmail.com" + "\", \"" + "clab.coreteam@gmail.com" + "\"]")
    private List<String> to;

    @Schema(description = "제목", example = "제목")
    private String subject;

    @Schema(description = "내용", example = "내용")
    private String content;


    public static EmailDto of(List<String> to, String subject, String content) {
        return EmailDto.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .build();
    }

}
