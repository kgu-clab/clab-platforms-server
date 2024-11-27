package page.clab.api.global.common.notificationSetting.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlatformType {

    SLACK("slack"),
    DISCORD("discord");

    private final String name;
}
