package page.clab.api.global.common.slack.domain;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = AlertTypeConverter.class)
    private AlertType alertType;

    private boolean enabled;

    public static NotificationSetting createDefault(AlertType alertType) {
        return NotificationSetting.builder()
                .alertType(alertType)
                .enabled(true)
                .build();
    }

    public void updateEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
