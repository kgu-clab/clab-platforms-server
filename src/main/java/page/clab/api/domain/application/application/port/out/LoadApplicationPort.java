package page.clab.api.domain.application.application.port.out;

import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;

import java.util.Optional;

public interface LoadApplicationPort {
    Optional<Application> findById(ApplicationId applicationId);
    Application findByIdOrThrow(ApplicationId applicationId);
}
