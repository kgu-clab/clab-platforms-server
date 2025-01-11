package page.clab.api.domain.hiring.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.dto.mapper.RecruitmentDtoMapper;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentDetailsResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRetrievalService implements RetrieveRecruitmentUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;
    private final RecruitmentDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public RecruitmentDetailsResponseDto retrieveRecruitmentDetails(Long id) {
        Recruitment recruitment = retrieveRecruitmentPort.getById(id);
        return mapper.toDetailsDto(recruitment);
    }
}
