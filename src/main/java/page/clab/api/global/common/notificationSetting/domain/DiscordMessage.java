package page.clab.api.global.common.notificationSetting.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscordMessage {

    private String content;
    private List<Embed> embeds;

    @Getter
    @Builder
    public static class Embed {

        private String title;
        private String description;
    }
}
