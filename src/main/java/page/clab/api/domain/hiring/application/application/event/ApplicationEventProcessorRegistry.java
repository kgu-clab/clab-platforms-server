package page.clab.api.domain.hiring.application.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationEventProcessorRegistry {

    private final List<ApplicationEventProcessor> processors = new ArrayList<>();

    public void register(ApplicationEventProcessor processor) {
        processors.add(processor);
    }

    public List<ApplicationEventProcessor> getProcessors() {
        return Collections.unmodifiableList(processors);
    }
}
