package page.clab.api.domain.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.port.in.RetrieveDeletedRecruitmentsUseCase;
import page.clab.api.domain.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedRecruitmentsRetrievalService implements RetrieveDeletedRecruitmentsUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<RecruitmentResponseDto> retrieveDeletedRecruitments(Pageable pageable) {
        Page<Recruitment> recruitments = retrieveRecruitmentPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(recruitments.map(RecruitmentResponseDto::toDto));
    }
}
