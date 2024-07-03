package page.clab.api.domain.login.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.BanMembersRetrievalUseCase;
import page.clab.api.domain.login.application.port.out.LoadAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.login.dto.response.AccountLockInfoResponseDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BanMembersRetrievalService implements BanMembersRetrievalUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final LoadAccountLockInfoPort loadAccountLockInfoPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccountLockInfoResponseDto> retrieve(Pageable pageable) {
        LocalDateTime banDate = LocalDateTime.of(9999, 12, 31, 23, 59);
        Page<AccountLockInfo> banMembers = loadAccountLockInfoPort.findByLockUntil(banDate, pageable);
        return new PagedResponseDto<>(banMembers.map(accountLockInfo -> {
            String memberName = memberInfoRetrievalUseCase.getMemberBasicInfoById(accountLockInfo.getMemberId()).getMemberName();
            return AccountLockInfoResponseDto.toDto(accountLockInfo, memberName);
        }));
    }
}