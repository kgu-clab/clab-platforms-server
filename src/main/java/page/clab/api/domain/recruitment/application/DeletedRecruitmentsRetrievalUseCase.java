package page.clab.api.domain.recruitment.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedRecruitmentsRetrievalUseCase {
    PagedResponseDto<RecruitmentResponseDto> retrieve(Pageable pageable);
}
