package page.clab.api.service;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.AccuseRepository;
import page.clab.api.type.dto.AccuseRequestDto;
import page.clab.api.type.dto.AccuseResponseDto;
import page.clab.api.type.entity.Accuse;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.AccuseStatus;
import page.clab.api.type.etc.TargetType;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccuseService {

    private final MemberService memberService;

    private final BoardService boardService;

    private final CommentService commentService;

    private final ReviewService reviewService;

    private final AccuseRepository accuseRepository;

    @Transactional
    public void createAccuse(AccuseRequestDto accuseRequestDto) {
        if (accuseRequestDto.getTargetType() == TargetType.BOARD) {
            boardService.getBoardByIdOrThrow(accuseRequestDto.getTargetId());
        } else if (accuseRequestDto.getTargetType() == TargetType.COMMENT) {
            commentService.getCommentByIdOrThrow(accuseRequestDto.getTargetId());
        } else if (accuseRequestDto.getTargetType() == TargetType.REVIEW) {
            reviewService.getReviewByIdOrThrow(accuseRequestDto.getTargetId());
        } else {
            throw new IllegalArgumentException("신고 대상 유형이 올바르지 않습니다.");
        }
        Member member = memberService.getCurrentMember();
        Accuse existingAccuse = getAccuseByMemberAndTargetTypeAndTargetId(member, accuseRequestDto);
        if (existingAccuse != null) {
            existingAccuse.setReason(accuseRequestDto.getReason());
            log.info("existingAccuse : {}", existingAccuse);
            accuseRepository.save(existingAccuse);
        } else {
            Accuse accuse = Accuse.of(accuseRequestDto);
            accuse.setMember(member);
            accuse.setAccuseStatus(AccuseStatus.PENDING);
            log.info("Accuse : {}", accuse);
            accuseRepository.save(accuse);
        }
    }

    public List<AccuseResponseDto> getAccuses(Pageable pageable) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("해당 신고 내역을 수정할 권한이 없습니다.");
        }
        Page<Accuse> accuses = accuseRepository.findAllByOrderByCreatedAtDesc(pageable);
        return accuses.map(AccuseResponseDto::of).getContent();
    }

    public List<AccuseResponseDto> searchAccuse(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("해당 신고 내역을 수정할 권한이 없습니다.");
        }
        Page<Accuse> accuses;
        if (targetType != null) {
            accuses = getAccuseByTargetType(targetType, pageable);
        } else if (accuseStatus != null) {
            accuses = getAccuseByAccuseStatus(accuseStatus, pageable);
        } else {
            throw new IllegalArgumentException("적어도 accuseType, accuseStatus 중 하나를 제공해야 합니다.");
        }
        if (accuses.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return accuses.map(AccuseResponseDto::of).getContent();
    }

    public void updateAccuseStatus(Long accuseId, AccuseStatus accuseStatus) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("해당 신고 내역을 수정할 권한이 없습니다.");
        }
        Accuse accuse = getAccuseByIdOrThrow(accuseId);
        accuse.setAccuseStatus(accuseStatus);
        accuseRepository.save(accuse);
    }

    private Accuse getAccuseByIdOrThrow(Long accuseId) {
        return accuseRepository.findById(accuseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고입니다."));
    }

    private Page<Accuse> getAccuseByTargetType(TargetType targetType, Pageable pageable) {
        return accuseRepository.findAllByTargetTypeOrderByCreatedAtDesc(targetType, pageable);
    }

    private Page<Accuse> getAccuseByAccuseStatus(AccuseStatus accuseStatus, Pageable pageable) {
        return accuseRepository.findAllByAccuseStatusOrderByCreatedAtDesc(accuseStatus, pageable);
    }

    private Accuse getAccuseByMemberAndTargetTypeAndTargetId(Member member, AccuseRequestDto accuseRequestDto) {
        return accuseRepository.findByMemberAndTargetTypeAndTargetId(member, accuseRequestDto.getTargetType(), accuseRequestDto.getTargetId())
                .orElse(null);
    }

}
