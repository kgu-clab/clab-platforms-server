package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.ApplicationPassCheckUseCase;
import page.clab.api.domain.application.application.port.out.LoadApplicationPort;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;

@Service
@RequiredArgsConstructor
public class ApplicationPassCheckService implements ApplicationPassCheckUseCase {

    private final LoadApplicationPort loadApplicationPort;

    @Transactional(readOnly = true)
    @Override
    public ApplicationPassResponseDto checkPassStatus(Long recruitmentId, String studentId) {
        ApplicationId id = ApplicationId.create(studentId, recruitmentId);
        return loadApplicationPort.findById(id)
                .map(ApplicationPassResponseDto::toDto)
                .orElseGet(() ->
                        ApplicationPassResponseDto.builder()
                                .isPass(false)
                                .build()
                );
    }
}
