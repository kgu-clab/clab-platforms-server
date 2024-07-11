package page.clab.api.domain.activity.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceRequestDto {

    private Long activityGroupId;

    private String QRCodeSecretKey;

}
