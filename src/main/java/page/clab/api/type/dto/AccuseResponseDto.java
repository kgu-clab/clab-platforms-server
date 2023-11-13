package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Accuse;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccuseResponseDto {

    private Long targetId;

    private String category;

    private String description;

    private String content;

    public static AccuseResponseDto of(Accuse accuse){
        return ModelMapperUtil.getModelMapper().map(accuse, AccuseResponseDto.class);
    }
}
