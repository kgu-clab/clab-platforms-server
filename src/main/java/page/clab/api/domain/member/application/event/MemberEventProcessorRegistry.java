package page.clab.api.domain.member.application.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MemberEventProcessorRegistry {

    private final List<MemberEventProcessor> processors = new ArrayList<>();

    public void register(MemberEventProcessor processor) {
        processors.add(processor);
    }

    public List<MemberEventProcessor> getProcessors() {
        return Collections.unmodifiableList(processors);
    }

}
