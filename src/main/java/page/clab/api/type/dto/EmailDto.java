package page.clab.api.type.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailDto {

    private String to;

    private String title;

    private String content;

}
