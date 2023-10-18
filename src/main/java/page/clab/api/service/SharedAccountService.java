package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.SharedAccountRepository;
import page.clab.api.type.dto.SharedAccountRequestDto;
import page.clab.api.type.dto.SharedAccountResponseDto;
import page.clab.api.type.entity.SharedAccount;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedAccountService {

    private final MemberService memberService;

    private final SharedAccountRepository sharedAccountRepository;

    public void createSharedAccount(SharedAccountRequestDto sharedAccountRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        SharedAccount sharedAccount = SharedAccount.of(sharedAccountRequestDto);
        sharedAccountRepository.save(sharedAccount);
    }

    public List<SharedAccountResponseDto> getSharedAccounts() {
        List<SharedAccount> sharedAccounts = sharedAccountRepository.findAll();
        return sharedAccounts.stream()
                .map(SharedAccountResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateSharedAccount(Long accountId, SharedAccountRequestDto sharedAccountRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        SharedAccount sharedAccount = getSharedAccountByIdOrThrow(accountId);
        SharedAccount updatedAccount = SharedAccount.of(sharedAccountRequestDto);
        updatedAccount.setId(sharedAccount.getId());
        sharedAccountRepository.save(updatedAccount);
    }

    public void deleteSharedAccount(Long accountId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        SharedAccount sharedAccount = getSharedAccountByIdOrThrow(accountId);
        sharedAccountRepository.delete(sharedAccount);
    }

    private SharedAccount getSharedAccountByIdOrThrow(Long accountId) {
        return sharedAccountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 계정입니다."));
    }

}
