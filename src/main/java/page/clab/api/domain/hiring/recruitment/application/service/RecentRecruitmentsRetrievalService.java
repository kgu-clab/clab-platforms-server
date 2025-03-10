package page.clab.api.domain.hiring.recruitment.application.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.dto.mapper.RecruitmentDtoMapper;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentEndDateResponseDto;
import page.clab.api.domain.hiring.recruitment.application.dto.response.RecruitmentResponseDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecentRecruitmentsUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;

@Service
@RequiredArgsConstructor
public class RecentRecruitmentsRetrievalService implements RetrieveRecentRecruitmentsUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;
    private final RecruitmentDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<RecruitmentResponseDto> retrieveRecentRecruitments() {
        return retrieveRecruitmentPort.findTop5ByOrderByCreatedAtDesc().stream()
            .map(mapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<RecruitmentEndDateResponseDto> retrieveRecruitmentsByEndDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1).with(LocalTime.MIN);
        LocalDateTime endOfDay = now.with(LocalTime.MAX);

        return retrieveRecruitmentPort.findByEndDateBetween(weekAgo, endOfDay).stream()
            .map(mapper::toEndDateDto)
            .toList();
    }
}
