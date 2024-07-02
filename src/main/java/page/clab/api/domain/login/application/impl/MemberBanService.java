package page.clab.api.domain.login.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.MemberBanUseCase;
import page.clab.api.domain.login.application.RedisTokenManagementUseCase;
import page.clab.api.domain.login.dao.AccountLockInfoRepository;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberBanService implements MemberBanUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RedisTokenManagementUseCase redisTokenManagementUseCase;
    private final SlackService slackService;
    private final AccountLockInfoRepository accountLockInfoRepository;

    @Transactional
    @Override
    public Long ban(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = memberLookupUseCase.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.banPermanently();
        redisTokenManagementUseCase.deleteByMemberId(memberId);
        sendSlackBanNotification(request, memberId);
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return accountLockInfoRepository.findByMemberId(memberId)
                .orElseGet(() -> createAccountLockInfo(memberId));
    }

    private AccountLockInfo createAccountLockInfo(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }

    private void sendSlackBanNotification(HttpServletRequest request, String memberId) {
        String memberName = memberLookupUseCase.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + memberId + ", Name: " + memberName);
    }
}