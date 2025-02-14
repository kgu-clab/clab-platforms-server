package page.clab.api.domain.hiring.recruitment.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.dto.mapper.RecruitmentDtoMapper;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentOpenResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveOpenRecruitmentsUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.RecruitmentStatus;

@Service
@RequiredArgsConstructor
public class OpenRecruitmentsRetrievalService implements RetrieveOpenRecruitmentsUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;
    private final RecruitmentDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<RecruitmentOpenResponseDto> retrieveOpenRecruitments() {
        return retrieveRecruitmentPort.findByStatus(RecruitmentStatus.OPEN).stream()
            .map(mapper::toOpenDto)
            .toList();
    }
}
