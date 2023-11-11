package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class NotificationRequestDto {

    @NotNull(message = "{notNull.notification.memberId}")
    private String memberId;

    @NotNull(message = "{notNull.notification.content}")
    @Size(min = 1, max = 1000, message = "{size.notification.content}")
    private String content;

}
