package page.clab.api.domain.members.support.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SupportPersistenceAdapter implements
        RegisterSupportPort,
        RetrieveSupportPort {

    private final SupportRepository supportRepository;
    private final SupportMapper supportMapper;

    @Override
    public Support getById(Long supportId) {
        return supportRepository.findById(supportId)
                .map(supportMapper::toDomain)
                .orElseThrow(
                        () -> new BaseException(ErrorCode.NOT_FOUND, "[Support] id: " + supportId + "에 해당하는 문의글이 존재하지 않습니다."));
    }

    @Override
    public Page<Support> findAllByKeywords(List<String> contents, Pageable pageable) {
        return supportRepository.findAllByKeywords(contents,pageable)
                .map(supportMapper::toDomain);
    }

    @Override
    public Page<Support> findAllByKeywordsAndTypeNotBug(List<String> contents, Pageable pageable) {
        return supportRepository.findAllByKeywordsAndIsCategoryNotBug(contents,pageable)
                .map(supportMapper::toDomain);
    }

    @Override
    public Page<Support> findAllByWriterId(String memberId, Pageable pageable) {
        return supportRepository.findAllByMemberIdAndIsDeletedFalse(memberId, pageable)
                .map(supportMapper::toDomain);
    }

    @Override
    public Support save(Support support) {
        SupportJpaEntity entity = supportMapper.toEntity(support);
        SupportJpaEntity savedEntity = supportRepository.save(entity);
        return supportMapper.toDomain(savedEntity);
    }
}
