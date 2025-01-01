package page.clab.api.domain.community.hashtag.domain;

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
public class Hashtag {

    Long id;
    String name;
    HashtagCategory hashtagCategory;
    Boolean isDeleted;
    Long boardUsage;

    public void incrementBoardUsage() {
        boardUsage++;
    }

    public void decreaseBoardUsage() {
        boardUsage = Math.max(0, boardUsage - 1);
    }
}

