package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Accuse;
import page.clab.api.type.etc.AccuseType;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccuseRequestDto {

    private AccuseType accuseType;

    private Long targetId;

    private String reason;

    public static AccuseRequestDto of(Accuse accuse){
        return ModelMapperUtil.getModelMapper().map(accuse, AccuseRequestDto.class);
    }

}
