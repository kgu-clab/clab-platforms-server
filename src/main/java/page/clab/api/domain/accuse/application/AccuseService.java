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
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccuseService {

    private final MemberService memberService;

    private final BoardService boardService;

    private final CommentService commentService;

    private final ReviewService reviewService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final AccuseRepository accuseRepository;

    @Transactional
    public Long createAccuse(AccuseRequestDto requestDto) {
        TargetType type = requestDto.getTargetType();
        Long accuseTargetId = requestDto.getTargetId();

        if (!isAccuseRequestValid(type, accuseTargetId)) {
            throw new NotFoundException(type.getDescription() + " " + accuseTargetId + "을 찾을 수 없습니다.");
        }

        Member currentMember = memberService.getCurrentMember();
        Accuse accuse = accuseRepository.findByMemberAndTargetTypeAndTargetId(currentMember, type, accuseTargetId)
                            .orElseGet(() -> AccuseRequestDto.toEntity(requestDto, currentMember));
        accuse.updateReason(requestDto.getReason());
        validationService.checkValid(accuse);

        notificationService.sendNotificationToMember(currentMember, "신고하신 내용이 접수되었습니다.");
        notificationService.sendNotificationToSuperAdmins(currentMember.getName() + "님이 신고를 접수하였습니다. 확인해주세요.");
        return accuseRepository.save(accuse).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AccuseResponseDto> getAccusesByConditions(TargetType type, AccuseStatus status, Pageable pageable) {
        Page<Accuse> accuses = accuseRepository.findByConditions(type, status, pageable);
        return new PagedResponseDto<>(accuses.map(AccuseResponseDto::toDto));
    }

    @Transactional
    public Long updateAccuseStatus(Long accuseId, AccuseStatus status) {
        Accuse accuse = getAccuseByIdOrThrow(accuseId);
        accuse.updateStatus(status);
        validationService.checkValid(accuse);
        notificationService.sendNotificationToMember(accuse.getMember(), "신고 상태가 " + status.getDescription() + "로 변경되었습니다.");
        return accuseRepository.save(accuse).getId();
    }

    private boolean isAccuseRequestValid(TargetType type, Long targetId) {
        if (type == TargetType.BOARD) {
            return boardService.isBoardExistById(targetId);
        }
        if (type == TargetType.COMMENT) {
            return commentService.isCommentExistById(targetId);
        }
        if (type == TargetType.REVIEW) {
            return reviewService.isReviewExistsById(targetId);
        }
        throw new AccuseTargetTypeIncorrectException("신고 대상 유형이 올바르지 않습니다.");
    }

    private Accuse getAccuseByIdOrThrow(Long accuseId) {
        return accuseRepository.findById(accuseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고입니다."));
    }

}
