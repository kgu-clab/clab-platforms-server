package page.clab.api.domain.community.board.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.port.out.RegisterHotBoardPort;
import page.clab.api.domain.community.board.application.port.out.RemoveHotBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveHotBoardPort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisHotBoardPersistenceAdapter implements
        RegisterHotBoardPort,
        RetrieveHotBoardPort,
        RemoveHotBoardPort {

    private static final String HOT_BOARDS_KEY = "hotBoards";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String boardId) {
        redisTemplate.opsForList().rightPush(HOT_BOARDS_KEY, boardId);
    }

    @Override
    public List<String> findAll() {
        List<String> hotBoards = redisTemplate.opsForList().range(HOT_BOARDS_KEY, 0, -1);
        return (hotBoards != null) ? hotBoards : List.of();
    }

    @Override
    public void clearHotBoard() {
        redisTemplate.delete(HOT_BOARDS_KEY);
    }
}
