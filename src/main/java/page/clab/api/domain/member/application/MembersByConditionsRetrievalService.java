package page.clab.api.domain.member.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface MembersByConditionsRetrievalService {
    PagedResponseDto<MemberResponseDto> retrieveByConditions(String id, String name, Pageable pageable);
}