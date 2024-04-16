package page.clab.api.domain.accuse.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.dao.AccuseTargetRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.dto.response.AccuseMemberInfo;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.domain.accuse.exception.AccuseTargetTypeIncorrectException;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.application.CommentService;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.review.application.ReviewService;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    private final AccuseTargetRepository accuseTargetRepository;

    @Transactional
    public Long createAccuse(AccuseRequestDto requestDto) {
        TargetType type = requestDto.getTargetType();
        Long targetId = requestDto.getTargetId();
        Member currentMember = memberService.getCurrentMember();

        validateAccuseRequest(type, targetId, currentMember);

        AccuseTarget target = getOrCreateAccuseTarget(requestDto, type, targetId);
        validationService.checkValid(target);
        accuseTargetRepository.save(target);

        Accuse accuse = findOrCreateAccuse(requestDto, currentMember, target);
        validationService.checkValid(accuse);

        notificationService.sendNotificationToMember(currentMember, "신고하신 내용이 접수되었습니다.");
        notificationService.sendNotificationToSuperAdmins(currentMember.getName() + "님이 신고를 접수하였습니다. 확인해주세요.");
        return accuseRepository.save(accuse).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AccuseResponseDto> getAccusesByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        Page<AccuseTarget> accuseTargets = accuseTargetRepository.findByConditions(type, status, countOrder, pageable);
        List<AccuseResponseDto> responseDtos = convertTargetsToResponseDtos(accuseTargets);
        return new PagedResponseDto<>(responseDtos, pageable, responseDtos.size());
    }

    @Transactional
    public Long updateAccuseStatus(TargetType type, Long targetId, AccuseStatus status) {
        AccuseTarget target = getAccuseTargetByIdOrThrow(type, targetId);
        target.updateStatus(status);
        validationService.checkValid(target);
        sendStatusUpdateNotifications(status, target);
        return accuseTargetRepository.save(target).getTargetReferenceId();
    }

    private AccuseTarget getAccuseTargetByIdOrThrow(TargetType type, Long targetId) {
        return accuseTargetRepository.findById(AccuseTargetId.create(type, targetId))
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고 대상입니다."));
    }

    private void validateAccuseRequest(TargetType type, Long targetId, Member currentMember) {
        switch (type) {
            case BOARD:
                Board board = boardService.getBoardByIdOrThrow(targetId);
                if (board.isOwner(currentMember)) {
                    throw new AccuseTargetTypeIncorrectException("자신의 게시글은 신고할 수 없습니다.");
                }
                break;
            case COMMENT:
                Comment comment = commentService.getCommentByIdOrThrow(targetId);
                if (comment.isOwner(currentMember)) {
                    throw new AccuseTargetTypeIncorrectException("자신의 댓글은 신고할 수 없습니다.");
                }
                break;
            case REVIEW:
                Review review = reviewService.getReviewByIdOrThrow(targetId);
                if (review.isOwner(currentMember)) {
                    throw new AccuseTargetTypeIncorrectException("자신의 리뷰는 신고할 수 없습니다.");
                }
                break;
            default:
                throw new AccuseTargetTypeIncorrectException("신고 대상 유형이 올바르지 않습니다.");
        }
    }

    private AccuseTarget getOrCreateAccuseTarget(AccuseRequestDto requestDto, TargetType type, Long targetId) {
        return accuseTargetRepository.findById(AccuseTargetId.create(type, targetId))
                .orElseGet(() -> AccuseRequestDto.toTargetEntity(requestDto));
    }

    private Accuse findOrCreateAccuse(AccuseRequestDto requestDto, Member currentMember, AccuseTarget target) {
        return accuseRepository.findByMemberAndTarget(currentMember, target)
                .map(existingAccuse -> {
                    existingAccuse.updateReason(requestDto.getReason());
                    return existingAccuse;
                })
                .orElseGet(() -> {
                    target.increaseAccuseCount();
                    return AccuseRequestDto.toEntity(requestDto, currentMember, target);
                });
    }

    @NotNull
    private List<AccuseResponseDto> convertTargetsToResponseDtos(Page<AccuseTarget> accuseTargets) {
        return accuseTargets.stream()
                .map(accuseTarget -> {
                    List<Accuse> accuses = accuseRepository.findByTargetOrderByCreatedAtDesc(accuseTarget);
                    List<AccuseMemberInfo> members = AccuseMemberInfo.create(accuses);
                    return AccuseResponseDto.toDto(accuses.getFirst(), members);
                })
                .toList();
    }

    private void sendStatusUpdateNotifications(AccuseStatus status, AccuseTarget target) {
        List<Member> members = accuseRepository.findByTarget(target).stream()
                .map(Accuse::getMember)
                .toList();
        notificationService.sendNotificationToMembers(members, "신고 상태가 " + status.getDescription() + "(으)로 변경되었습니다.");
    }

}
