package page.clab.api.domain.activityGroup.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRequestDto {

    private Long activityGroupId;

    private String QRCodeSecretKey;

}
