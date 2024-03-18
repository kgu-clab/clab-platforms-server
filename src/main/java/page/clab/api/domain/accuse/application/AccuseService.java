package page.clab.api.domain.accuse.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.domain.accuse.exception.AccuseTargetTypeIncorrectException;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.comment.application.CommentService;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.review.application.ReviewService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

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
        TargetType accuseTargetType = accuseRequestDto.getTargetType();
        Long accuseTargetId = accuseRequestDto.getTargetId();

        if (!isAccuseRequestValid(accuseTargetType, accuseTargetId)) {
            throw new NotFoundException(accuseTargetType.getDescription() + " " + accuseTargetId + "을 찾을 수 없습니다.");
        }

        Member member = memberService.getCurrentMember();
        Accuse accuse = accuseRepository.findByMemberAndTargetTypeAndTargetId(member, accuseTargetType, accuseTargetId)
                            .orElseGet(() -> Accuse.create(accuseRequestDto, member));
        accuse.updateReason(accuseRequestDto.getReason());

        notificationService.sendNotificationToMember(member, "신고하신 내용이 접수되었습니다.");
        notificationService.sendNotificationToSuperAdmins(member.getName() + "님이 신고를 접수하였습니다. 확인해주세요.");
        return accuseRepository.save(accuse).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AccuseResponseDto> getAccusesByConditions(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable) {
        Page<Accuse> accuses = accuseRepository.findByConditions(targetType, accuseStatus, pageable);
        return new PagedResponseDto<>(accuses.map(AccuseResponseDto::of));
    }

    @Transactional
    public Long updateAccuseStatus(Long accuseId, AccuseStatus accuseStatus) {
        Accuse accuse = getAccuseByIdOrThrow(accuseId);
        accuse.updateStatus(accuseStatus);
        notificationService.sendNotificationToMember(accuse.getMember(), "신고 상태가 " + accuseStatus.getDescription() + "로 변경되었습니다.");
        return accuseRepository.save(accuse).getId();
    }

    private boolean isAccuseRequestValid(TargetType accuseTargetType, Long accuseTargetId) {
        if (accuseTargetType == TargetType.BOARD) {
            return boardService.isBoardExistById(accuseTargetId);
        }
        if (accuseTargetType == TargetType.COMMENT) {
            return commentService.isCommentExistById(accuseTargetId);
        }
        if (accuseTargetType == TargetType.REVIEW) {
            return reviewService.isReviewExistsById(accuseTargetId);
        }
        throw new AccuseTargetTypeIncorrectException("신고 대상 유형이 올바르지 않습니다.");
    }

    private Accuse getAccuseByIdOrThrow(Long accuseId) {
        return accuseRepository.findById(accuseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고입니다."));
    }

}
