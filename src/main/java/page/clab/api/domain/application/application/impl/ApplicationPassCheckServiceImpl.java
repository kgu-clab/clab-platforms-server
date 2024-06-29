package page.clab.api.domain.application.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.ApplicationPassCheckService;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;

@Service
@RequiredArgsConstructor
public class ApplicationPassCheckServiceImpl implements ApplicationPassCheckService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    @Override
    public ApplicationPassResponseDto checkPassStatus(Long recruitmentId, String studentId) {
        ApplicationId id = ApplicationId.create(studentId, recruitmentId);
        return applicationRepository.findById(id)
                .map(ApplicationPassResponseDto::toDto)
                .orElseGet(() ->
                        ApplicationPassResponseDto.builder()
                            .isPass(false)
                            .build()
                );
    }
}