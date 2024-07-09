package page.clab.api.domain.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.application.port.in.CheckApplicationPassStatusUseCase;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.application.domain.ApplicationId;

@Service
@RequiredArgsConstructor
public class ApplicationPassCheckService implements CheckApplicationPassStatusUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;

    @Transactional(readOnly = true)
    @Override
    public ApplicationPassResponseDto checkPassStatus(Long recruitmentId, String studentId) {
        ApplicationId id = ApplicationId.create(studentId, recruitmentId);
        return retrieveApplicationPort.findById(id)
                .map(ApplicationPassResponseDto::toDto)
                .orElseGet(() ->
                        ApplicationPassResponseDto.builder()
                                .isPass(false)
                                .build()
                );
    }
}
