package page.clab.api.domain.community.board.application.port.out;

import java.util.List;

public interface RetrieveHotBoardPort {
    List<String> findAll();
}
