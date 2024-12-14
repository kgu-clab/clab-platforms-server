package page.clab.api.domain.community.board.domain;

import java.time.LocalDateTime;
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
public class BoardHashtag {

    Long id;
    Long boardId;
    Long hashtagId;
    Boolean isDeleted;

    public void toggleIsDeletedStatus() {
        this.isDeleted = !this.isDeleted;
    }
}
