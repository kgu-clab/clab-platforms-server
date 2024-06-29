package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchDeletedRecruitmentsServiceImpl implements FetchDeletedRecruitmentsService {

    private final RecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<RecruitmentResponseDto> execute(Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(recruitments.map(RecruitmentResponseDto::toDto));
    }
}
