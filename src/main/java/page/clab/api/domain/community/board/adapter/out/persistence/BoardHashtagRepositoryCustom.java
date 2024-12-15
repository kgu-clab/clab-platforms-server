package page.clab.api.domain.community.board.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardHashtagRepositoryCustom {
    List<Long> getBoardIdsByHashTagId(List<Long> hashtagIds, Pageable pageable);
}
