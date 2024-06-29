package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchRecentRecruitmentsServiceImpl implements FetchRecentRecruitmentsService {

    private final RecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RecruitmentResponseDto> execute() {
        List<Recruitment> recruitments = recruitmentRepository.findTop5ByOrderByCreatedAtDesc();
        return recruitments.stream()
                .map(RecruitmentResponseDto::toDto)
                .toList();
    }
}
