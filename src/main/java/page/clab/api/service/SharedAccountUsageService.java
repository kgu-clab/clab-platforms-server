package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.exception.AccountInUseException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SharedAccountUsageStateException;
import page.clab.api.repository.SharedAccountUsageRepository;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.SharedAccountUsageRequestDto;
import page.clab.api.type.dto.SharedAccountUsageResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.SharedAccount;
import page.clab.api.type.entity.SharedAccountUsage;
import page.clab.api.type.etc.SharedAccountUsageStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class SharedAccountUsageService {

    private final SharedAccountService sharedAccountService;

    private final MemberService memberService;

    private final SharedAccountUsageRepository sharedAccountUsageRepository;

    @Transactional
    public Long requestSharedAccountUsage(SharedAccountUsageRequestDto sharedAccountUsageRequestDto) {
        Long sharedAccountId = sharedAccountUsageRequestDto.getSharedAccountId();
        SharedAccount sharedAccount = sharedAccountService.getSharedAccountByIdOrThrow(sharedAccountId);
        if (sharedAccount.isInUse()) {
            throw new AccountInUseException("이미 이용 중인 계정입니다.");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startTime = sharedAccountUsageRequestDto.getStartTime();
        LocalDateTime endTime = sharedAccountUsageRequestDto.getEndTime();
        if (startTime.isBefore(currentDateTime)) {
            throw new IllegalArgumentException("이용 시작 시간은 현재 시간 이후여야 합니다.");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("이용 종료 시간은 시작 시간 이후여야 합니다.");
        }
        String memberId = memberService.getCurrentMember().getId();
        SharedAccountUsage sharedAccountUsage = SharedAccountUsage.of(sharedAccountUsageRequestDto);
        sharedAccountUsage.setId(null);
        sharedAccountUsage.setSharedAccount(sharedAccount);
        sharedAccountUsage.setMemberId(memberId);
        sharedAccountUsage.setStatus(SharedAccountUsageStatus.IN_USE);
        sharedAccount.setInUse(true);
        sharedAccountUsageRepository.save(sharedAccountUsage);
        return sharedAccountService.save(sharedAccount).getId();
    }

    public PagedResponseDto<SharedAccountUsageResponseDto> getSharedAccountUsages(Pageable pageable) {
        Page<SharedAccountUsage> sharedAccountUsages = sharedAccountUsageRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(sharedAccountUsages.map(SharedAccountUsageResponseDto::of));
    }

    @Transactional
    public Long cancelSharedAccountUsage(Long usageId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        SharedAccountUsage sharedAccountUsage = getSharedAccountUsageByIdOrThrow(usageId);
        if (!(sharedAccountUsage.getMemberId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("공유 계정 이용 취소 권한이 없습니다.");
        }
        if (!sharedAccountUsage.getStatus().equals(SharedAccountUsageStatus.IN_USE)) {
            throw new SharedAccountUsageStateException("이용 중인 공유 계정만 취소가 가능합니다.");
        }
        SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
        sharedAccount.setInUse(false);
        sharedAccountUsage.setStatus(SharedAccountUsageStatus.CANCELED);
        sharedAccountUsageRepository.save(sharedAccountUsage);
        return sharedAccountService.save(sharedAccount).getId();
    }

    @Transactional
    public Long completeSharedAccountUsage(Long usageId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        SharedAccountUsage sharedAccountUsage = getSharedAccountUsageByIdOrThrow(usageId);
        if (!(sharedAccountUsage.getMemberId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("공유 계정 이용 완료 권한이 없습니다.");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (sharedAccountUsage.getStatus().equals(SharedAccountUsageStatus.COMPLETED)) {
            throw new SharedAccountUsageStateException("이미 완료된 공유 계정 이용 내역입니다.");
        }
        SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
        sharedAccount.setInUse(false);
        sharedAccountUsage.setEndTime(currentDateTime);
        sharedAccountUsage.setStatus(SharedAccountUsageStatus.COMPLETED);
        sharedAccountUsageRepository.save(sharedAccountUsage);
        return sharedAccountService.save(sharedAccount).getId();
    }

    @Scheduled(fixedRate = 60000)
    public void findAndUpdateCompletedStatus() {
        List<SharedAccountUsage> inUseSharedAccountUsages = getSharedAccountUsageByStatusAndEndTimeBefore();
        for (SharedAccountUsage sharedAccountUsage : inUseSharedAccountUsages) {
            int retryCount = 0;
            boolean isProcessingSuccessful = false;
            while (retryCount < 3 && !isProcessingSuccessful) {
                try {
                    SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
                    sharedAccount.setInUse(false);
                    sharedAccountUsage.setStatus(SharedAccountUsageStatus.COMPLETED);
                    sharedAccountService.save(sharedAccount);
                    sharedAccountUsageRepository.save(sharedAccountUsage);
                    isProcessingSuccessful = true;
                } catch (Exception e) {
                    retryCount++;
                }
            }
            if (!isProcessingSuccessful) {
                log.error("공유 계정 이용 완료 처리에 실패했습니다. 공유 계정 이용 내역 ID: {}", sharedAccountUsage.getId());
            }
        }
    }

    public SharedAccountUsage getSharedAccountUsageByIdOrThrow(Long usageId) {
        return sharedAccountUsageRepository.findById(usageId)
                .orElseThrow(() -> new NotFoundException("공유 계정 이용 내역을 찾을 수 없습니다."));
    }

    public List<SharedAccountUsage> getSharedAccountUsageByStatusAndEndTimeBefore() {
        return sharedAccountUsageRepository
                .findByStatusAndEndTimeBefore(SharedAccountUsageStatus.IN_USE, LocalDateTime.now());
    }

}
