package page.clab.api.domain.recruitment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.RecentRecruitmentsRetrievalUseCase;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentRecruitmentsRetrievalService implements RecentRecruitmentsRetrievalUseCase {

    private final RecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RecruitmentResponseDto> retrieve() {
        List<Recruitment> recruitments = recruitmentRepository.findTop5ByOrderByCreatedAtDesc();
        return recruitments.stream()
                .map(RecruitmentResponseDto::toDto)
                .toList();
    }
}
