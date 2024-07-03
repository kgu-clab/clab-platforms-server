package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.RetrieveDonationsByConditionsUseCase;
import page.clab.api.domain.donation.application.port.out.RetrieveDonationsByConditionsPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DonationsByConditionsRetrievalService implements RetrieveDonationsByConditionsUseCase {

    private final RetrieveDonationsByConditionsPort retrieveDonationsByConditionsPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> retrieve(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Donation> donations = retrieveDonationsByConditionsPort.findByConditions(memberId, name, startDate, endDate, pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = retrieveMemberInfoUseCase.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }
}
