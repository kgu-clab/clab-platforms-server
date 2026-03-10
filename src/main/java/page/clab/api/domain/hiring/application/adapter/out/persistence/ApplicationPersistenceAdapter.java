package page.clab.api.domain.hiring.application.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.hiring.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceAdapter implements
    RegisterApplicationPort,
    RetrieveApplicationPort {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    public Application save(Application application) {
        ApplicationJpaEntity entity = applicationMapper.toEntity(application);
        ApplicationJpaEntity savedEntity = applicationRepository.save(entity);
        return applicationMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Application> findById(ApplicationId applicationId) {
        return applicationRepository.findById(applicationId)
            .map(applicationMapper::toDomain);
    }

    @Override
    public Application getById(ApplicationId applicationId) {
        return applicationRepository.findById(applicationId)
            .map(applicationMapper::toDomain)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND,
                "[Application] id: " + applicationId + "에 해당하는 지원서가 존재하지 않습니다."));
    }

    @Override
    public Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        return applicationRepository.findByConditions(recruitmentId, studentId, isPass, pageable)
            .map(applicationMapper::toDomain);
    }

    @Override
    public List<Application> findByRecruitmentIdAndIsPass(Long recruitmentId, boolean isPass) {
        return applicationRepository.findByRecruitmentIdAndIsPass(recruitmentId, isPass).stream()
            .map(applicationMapper::toDomain)
            .toList();
    }

    @Override
    public Application getByRecruitmentIdAndStudentId(Long recruitmentId, String studentId) {
        return applicationRepository.findByRecruitmentIdAndStudentId(recruitmentId, studentId)
            .map(applicationMapper::toDomain)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND,
                "[Application] recruitmentId: " + recruitmentId + ", studentId: " + studentId
                    + "에 해당하는 지원서가 존재하지 않습니다."));
    }
}
