package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchApplicationsServiceImpl implements FetchApplicationsService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ApplicationResponseDto> execute(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        Page<Application> applications = applicationRepository.findByConditions(recruitmentId, studentId, isPass, pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }
}