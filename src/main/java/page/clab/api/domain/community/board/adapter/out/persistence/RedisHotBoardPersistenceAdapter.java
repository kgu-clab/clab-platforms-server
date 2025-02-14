package page.clab.api.domain.community.board.adapter.out.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.port.out.RegisterHotBoardPort;
import page.clab.api.domain.community.board.application.port.out.RemoveHotBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveHotBoardPort;

@Component
@RequiredArgsConstructor
public class RedisHotBoardPersistenceAdapter implements
        RegisterHotBoardPort,
        RetrieveHotBoardPort,
        RemoveHotBoardPort {

    private static final String HOT_BOARDS_PREFIX = "hotBoards";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String boardId, String strategyName) {
        String key = getRedisKey(strategyName);
        redisTemplate.opsForList().rightPush(key, boardId);
    }

    @Override
    public List<String> findByHotBoardStrategy(String strategyName) {
        String key = getRedisKey(strategyName);
        List<String> hotBoards = redisTemplate.opsForList().range(key, 0, -1);
        return (hotBoards != null) ? hotBoards : List.of();
    }

    @Override
    public void clearHotBoard() {
        String pattern = HOT_BOARDS_PREFIX + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null) {
            return;
        }
        keys.stream()
                .filter(Objects::nonNull)
                .forEach(key -> {
                    Long size = redisTemplate.opsForList().size(key);
                    if (size != null && size > 0) {
                        redisTemplate.delete(key);
                    }
                });
    }

    private String getRedisKey(String strategyName) {
        return String.format("%s:%s", HOT_BOARDS_PREFIX, strategyName);
    }
}
