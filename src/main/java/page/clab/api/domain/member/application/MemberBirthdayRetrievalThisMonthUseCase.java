package page.clab.api.domain.member.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface MemberBirthdayRetrievalThisMonthUseCase {
    PagedResponseDto<MemberBirthdayResponseDto> retrieve(int month, Pageable pageable);
}