package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String subTitle;

    @NotNull
    private String content;

    @URL
    private String imageUrl;

    private String tag;

}
