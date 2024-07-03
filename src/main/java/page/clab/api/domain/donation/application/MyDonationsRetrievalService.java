package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.MyDonationsRetrievalUseCase;
import page.clab.api.domain.donation.application.port.out.RetrieveMyDonationsPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyDonationsRetrievalService implements MyDonationsRetrievalUseCase {

    private final RetrieveMyDonationsPort retrieveMyDonationsPort;
    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberRetrievalUseCase.getCurrentMemberId();
        Page<Donation> donations = retrieveMyDonationsPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = memberInfoRetrievalUseCase.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }
}
