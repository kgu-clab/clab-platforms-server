package page.clab.api.domain.member.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MemberEventProcessorRegistry {

    private final List<MemberEventProcessor> processors = new ArrayList<>();

    public void registerProcessor(MemberEventProcessor processor) {
        processors.add(processor);
    }

    public List<MemberEventProcessor> getProcessors() {
        return Collections.unmodifiableList(processors);
    }

}
