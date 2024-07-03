package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.port.in.RetrieveRecentRecruitmentsUseCase;
import page.clab.api.domain.recruitment.application.port.out.RetrieveRecentRecruitmentsPort;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentRecruitmentsRetrievalService implements RetrieveRecentRecruitmentsUseCase {

    private final RetrieveRecentRecruitmentsPort retrieveRecentRecruitmentsPort;

    @Transactional(readOnly = true)
    @Override
    public List<RecruitmentResponseDto> retrieve() {
        return retrieveRecentRecruitmentsPort.findTop5ByOrderByCreatedAtDesc().stream()
                .map(RecruitmentResponseDto::toDto)
                .toList();
    }
}
