package page.clab.api.domain.recruitment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.recruitment.application.RecruitmentLookupService;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class RecruitmentLookupServiceImpl implements RecruitmentLookupService {

    private final RecruitmentRepository recruitmentRepository;

    @Override
    public Recruitment getRecruitmentByIdOrThrow(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NotFoundException("[Recruitment] id: " + recruitmentId + "에 해당하는 모집 공고가 존재하지 않습니다."));
    }
}
