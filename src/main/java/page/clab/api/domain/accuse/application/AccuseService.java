package page.clab.api.domain.accuse.application;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.comment.application.CommentService;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.domain.review.application.ReviewService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.SearchResultNotExistException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccuseService {

    private final MemberService memberService;

    private final BoardService boardService;

    private final CommentService commentService;

    private final ReviewService reviewService;

    private final NotificationService notificationService;

    private final AccuseRepository accuseRepository;

    @Transactional
    public Long createAccuse(AccuseRequestDto accuseRequestDto) {
        if (accuseRequestDto.getTargetType() == TargetType.BOARD) {
            boardService.getBoardByIdOrThrow(accuseRequestDto.getTargetId());
        } else if (accuseRequestDto.getTargetType() == TargetType.COMMENT) {
            commentService.getCommentByIdOrThrow(accuseRequestDto.getTargetId());
        } else if (accuseRequestDto.getTargetType() == TargetType.REVIEW) {
            reviewService.getReviewByIdOrThrow(accuseRequestDto.getTargetId());
        } else {
            throw new IllegalArgumentException("신고 대상 유형이 올바르지 않습니다.");
        }
        Long id;
        Member member = memberService.getCurrentMember();
        Accuse existingAccuse = getAccuseByMemberAndTargetTypeAndTargetId(member, accuseRequestDto);
        if (existingAccuse != null) {
            existingAccuse.setReason(accuseRequestDto.getReason());
            id = accuseRepository.save(existingAccuse).getId();
        } else {
            Accuse accuse = Accuse.of(accuseRequestDto);
            accuse.setId(null);
            accuse.setMember(member);
            accuse.setAccuseStatus(AccuseStatus.PENDING);
            id = accuseRepository.save(accuse).getId();
        }

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .content("신고하신 내용이 접수되었습니다.")
                .memberId(member.getId())
                .build();
        notificationService.createNotification(notificationRequestDto);

        List<Member> superMembers = memberService.getMembersByRole(Role.SUPER);
        for (Member superMember : superMembers) {
            NotificationRequestDto notificationRequestDtoForSuper = NotificationRequestDto.builder()
                    .content(member.getName() + "님이 신고를 접수하였습니다. 확인해주세요.")
                    .memberId(superMember.getId())
                    .build();
            notificationService.createNotification(notificationRequestDtoForSuper);
        }
        return id;
    }

    public PagedResponseDto<AccuseResponseDto> getAccuses(Pageable pageable) {
        Page<Accuse> accuses = accuseRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(accuses.map(AccuseResponseDto::of));
    }

    public PagedResponseDto<AccuseResponseDto> searchAccuse(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable) {
        Page<Accuse> accuses;
        if (targetType != null && accuseStatus != null) {
            accuses = getAccuseByTargetTypeAndAccuseStatus(targetType, accuseStatus, pageable);
        } else if (targetType != null) {
            accuses = getAccuseByTargetType(targetType, pageable);
        } else if (accuseStatus != null) {
            accuses = getAccuseByAccuseStatus(accuseStatus, pageable);
        } else {
            throw new IllegalArgumentException("적어도 accuseType, accuseStatus 중 하나를 제공해야 합니다.");
        }
        if (accuses.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return new PagedResponseDto<>(accuses.map(AccuseResponseDto::of));
    }

    public Long updateAccuseStatus(Long accuseId, AccuseStatus accuseStatus) {
        Accuse accuse = getAccuseByIdOrThrow(accuseId);
        accuse.setAccuseStatus(accuseStatus);
        Long id = accuseRepository.save(accuse).getId();
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .content("신고 상태가 " + accuseStatus + "로 변경되었습니다.")
                .memberId(accuse.getMember().getId())
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    private Accuse getAccuseByIdOrThrow(Long accuseId) {
        return accuseRepository.findById(accuseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고입니다."));
    }

    private Page<Accuse> getAccuseByTargetType(TargetType targetType, Pageable pageable) {
        return accuseRepository.findAllByTargetTypeOrderByCreatedAtDesc(targetType, pageable);
    }

    private Page<Accuse> getAccuseByTargetTypeAndAccuseStatus(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable) {
        return accuseRepository.findAllByTargetTypeAndAccuseStatusOrderByCreatedAtDesc(targetType, accuseStatus, pageable);
    }

    private Page<Accuse> getAccuseByAccuseStatus(AccuseStatus accuseStatus, Pageable pageable) {
        return accuseRepository.findAllByAccuseStatusOrderByCreatedAtDesc(accuseStatus, pageable);
    }

    private Accuse getAccuseByMemberAndTargetTypeAndTargetId(Member member, AccuseRequestDto accuseRequestDto) {
        return accuseRepository.findByMemberAndTargetTypeAndTargetId(member, accuseRequestDto.getTargetType(), accuseRequestDto.getTargetId())
                .orElse(null);
    }

}
