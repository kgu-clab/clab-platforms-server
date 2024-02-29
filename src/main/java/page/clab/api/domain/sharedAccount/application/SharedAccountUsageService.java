package page.clab.api.domain.sharedAccount.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.sharedAccount.dao.SharedAccountUsageRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsage;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUsageRequestDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountUsageResponseDto;
import page.clab.api.domain.sharedAccount.exception.InvalidUsageTimeException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountUsageStateException;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SharedAccountUsageService {

    private final SharedAccountService sharedAccountService;

    private final MemberService memberService;

    private final SharedAccountUsageRepository sharedAccountUsageRepository;

    @Transactional
    public Long requestSharedAccountUsage(SharedAccountUsageRequestDto sharedAccountUsageRequestDto) throws CustomOptimisticLockingFailureException {
        Long sharedAccountId = sharedAccountUsageRequestDto.getSharedAccountId();
        SharedAccount sharedAccount = sharedAccountService.getSharedAccountByIdOrThrow(sharedAccountId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startTime = sharedAccountUsageRequestDto.getStartTime();
        LocalDateTime endTime = sharedAccountUsageRequestDto.getEndTime();
        if (startTime.isBefore(currentDateTime)) {
            throw new InvalidUsageTimeException("이용 시작 시간은 현재 시간 이후여야 합니다.");
        }
        if (endTime.isBefore(startTime)) {
            throw new InvalidUsageTimeException("이용 종료 시간은 시작 시간 이후여야 합니다.");
        }
        try {
            String memberId = memberService.getCurrentMember().getId();
            SharedAccountUsage sharedAccountUsage = SharedAccountUsage.of(sharedAccountUsageRequestDto);
            sharedAccountUsage.setId(null);
            sharedAccountUsage.setSharedAccount(sharedAccount);
            sharedAccountUsage.setMemberId(memberId);
            List<SharedAccountUsage> reservedSharedAccountUsages = sharedAccountUsageRepository
                    .findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(
                            sharedAccountId, SharedAccountUsageStatus.RESERVED, endTime, startTime);
            if (!reservedSharedAccountUsages.isEmpty()) {
                throw new SharedAccountUsageStateException("해당 시간대와 겹치는 예약 내역이 있습니다. 다른 시간대를 선택해주세요.");
            }
            List<SharedAccountUsage> inUseSharedAccountUsages = sharedAccountUsageRepository
                    .findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(
                            sharedAccountId, SharedAccountUsageStatus.IN_USE, endTime, startTime);
            if (!inUseSharedAccountUsages.isEmpty()) {
                throw new SharedAccountUsageStateException("해당 시간대에 이미 이용 중인 공유 계정이 있습니다. 다른 시간대를 선택해주세요.");
            }
            if (currentDateTime.isAfter(startTime) && currentDateTime.isBefore(endTime)) {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.IN_USE);
                sharedAccount.setInUse(true);
            } else {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.RESERVED);
            }
            sharedAccountUsageRepository.save(sharedAccountUsage);
            return sharedAccountService.save(sharedAccount).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
              throw new CustomOptimisticLockingFailureException("공유 계정 이용 요청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    public PagedResponseDto<SharedAccountUsageResponseDto> getSharedAccountUsages(Pageable pageable) {
        Page<SharedAccountUsage> sharedAccountUsages = sharedAccountUsageRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(sharedAccountUsages.map(SharedAccountUsageResponseDto::of));
    }

    @Transactional
    public Long updateSharedAccountUsage(Long usageId, SharedAccountUsageStatus status) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        SharedAccountUsage sharedAccountUsage = getSharedAccountUsageByIdOrThrow(usageId);
        if (!(sharedAccountUsage.getMemberId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("공유 계정 이용 상태 변경 권한이 없습니다.");
        }
        if (SharedAccountUsageStatus.IN_USE.equals(sharedAccountUsage.getStatus())) {
            if (SharedAccountUsageStatus.CANCELED.equals(status) || SharedAccountUsageStatus.COMPLETED.equals(status)) {
                SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
                sharedAccount.setInUse(false);
                sharedAccountUsage.setStatus(status);
                sharedAccountService.save(sharedAccount);
                sharedAccountUsageRepository.save(sharedAccountUsage);
                return sharedAccount.getId();
            }
        } else if (SharedAccountUsageStatus.RESERVED.equals(sharedAccountUsage.getStatus())) {
            if (SharedAccountUsageStatus.CANCELED.equals(status)) {
                sharedAccountUsage.setStatus(status);
                sharedAccountUsageRepository.save(sharedAccountUsage);
                return sharedAccountUsage.getId();
            }
            throw new SharedAccountUsageStateException("예약된 공유 계정은 취소만 가능합니다.");
        }
        throw new SharedAccountUsageStateException("이용 중 취소/완료, 예약 취소만 가능합니다.");
    }

    @Scheduled(fixedRate = 60000)
    public void updateSharedAccountUsageStatus() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<SharedAccountUsage> reservedSharedAccountUsages = sharedAccountUsageRepository
                .findByStatusAndStartTimeBefore(SharedAccountUsageStatus.RESERVED, currentDateTime);
        for (SharedAccountUsage reservedSharedAccountUsage : reservedSharedAccountUsages) {
            reservedSharedAccountUsage.setStatus(SharedAccountUsageStatus.IN_USE);
            reservedSharedAccountUsage.getSharedAccount().setInUse(true);
            sharedAccountUsageRepository.save(reservedSharedAccountUsage);
            sharedAccountService.save(reservedSharedAccountUsage.getSharedAccount());
            log.info("{}: 공유 계정 이용 내역이 이용 중으로 변경되었습니다.", reservedSharedAccountUsage.getSharedAccount().getUsername());
        }
        List<SharedAccountUsage> inUseSharedAccountUsages = sharedAccountUsageRepository
                .findByStatusAndEndTimeBefore(SharedAccountUsageStatus.IN_USE, currentDateTime);
        for (SharedAccountUsage inUseSharedAccountUsage : inUseSharedAccountUsages) {
            inUseSharedAccountUsage.setStatus(SharedAccountUsageStatus.COMPLETED);
            inUseSharedAccountUsage.getSharedAccount().setInUse(false);
            sharedAccountUsageRepository.save(inUseSharedAccountUsage);
            sharedAccountService.save(inUseSharedAccountUsage.getSharedAccount());
            log.info("{}: 공유 계정 이용 내역이 완료로 변경되었습니다.", inUseSharedAccountUsage.getSharedAccount().getUsername());
        }
    }

    public SharedAccountUsage getSharedAccountUsageByIdOrThrow(Long usageId) {
        return sharedAccountUsageRepository.findById(usageId)
                .orElseThrow(() -> new NotFoundException("공유 계정 이용 내역을 찾을 수 없습니다."));
    }

}
