package page.clab.api.domain.community.board.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardEventProcessorRegistry {

    private final List<BoardEventProcessor> processors = new ArrayList<>();

    public void register(BoardEventProcessor processor) {
        processors.add(processor);
    }

    public List<BoardEventProcessor> getProcessors() {
        return Collections.unmodifiableList(processors);
    }
}
