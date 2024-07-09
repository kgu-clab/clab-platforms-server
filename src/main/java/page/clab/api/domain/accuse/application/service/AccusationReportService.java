package page.clab.api.domain.accuse.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.application.exception.AccuseTargetTypeIncorrectException;
import page.clab.api.domain.accuse.application.port.in.ReportAccusationUseCase;
import page.clab.api.domain.accuse.application.port.out.RegisterAccusePort;
import page.clab.api.domain.accuse.application.port.out.RegisterAccuseTargetPort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseTargetPort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.board.application.port.in.RetrieveBoardsUseCase;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.application.port.in.RetrieveCommentsUseCase;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.domain.review.application.port.in.RetrieveReviewUseCase;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class AccusationReportService implements ReportAccusationUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final SendNotificationUseCase notificationService;
    private final RegisterAccusePort registerAccusePort;
    private final RegisterAccuseTargetPort registerAccuseTargetPort;
    private final RetrieveAccusePort retrieveAccusePort;
    private final RetrieveAccuseTargetPort retrieveAccuseTargetPort;
    private final RetrieveBoardsUseCase retrieveBoardsUseCase;
    private final RetrieveCommentsUseCase retrieveCommentsUseCase;
    private final RetrieveReviewUseCase retrieveReviewUseCase;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long reportAccusation(AccuseRequestDto requestDto) {
        TargetType type = requestDto.getTargetType();
        Long targetId = requestDto.getTargetId();
        String memberId = retrieveMemberUseCase.getCurrentMemberId();

        validateAccusationRequest(type, targetId, memberId);

        AccuseTarget target = getOrCreateAccuseTarget(requestDto, type, targetId);
        validationService.checkValid(target);
        registerAccuseTargetPort.save(target);

        Accuse accuse = findOrCreateAccusation(requestDto, memberId, target);
        validationService.checkValid(accuse);

        notificationService.sendNotificationToMember(memberId, "신고하신 내용이 접수되었습니다.");
        notificationService.sendNotificationToSuperAdmins(memberId + "님이 신고를 접수하였습니다. 확인해주세요.");
        return registerAccusePort.save(accuse).getId();
    }

    private void validateAccusationRequest(TargetType type, Long targetId, String currentMemberId) {
        switch (type) {
            case BOARD:
                Board board = retrieveBoardsUseCase.findByIdOrThrow(targetId);
                if (board.isOwner(currentMemberId)) {
                    throw new AccuseTargetTypeIncorrectException("자신의 게시글은 신고할 수 없습니다.");
                }
                break;
            case COMMENT:
                Comment comment = retrieveCommentsUseCase.findByIdOrThrow(targetId);
                if (comment.isOwner(currentMemberId)) {
                    throw new AccuseTargetTypeIncorrectException("자신의 댓글은 신고할 수 없습니다.");
                }
                break;
            case REVIEW:
                Review review = retrieveReviewUseCase.findByIdOrThrow(targetId);
                if (review.isOwner(currentMemberId)) {
                    throw new AccuseTargetTypeIncorrectException("자신의 리뷰는 신고할 수 없습니다.");
                }
                break;
            default:
                throw new AccuseTargetTypeIncorrectException("신고 대상 유형이 올바르지 않습니다.");
        }
    }

    private AccuseTarget getOrCreateAccuseTarget(AccuseRequestDto requestDto, TargetType type, Long targetId) {
        return retrieveAccuseTargetPort.findById(AccuseTargetId.create(type, targetId))
                .orElseGet(() -> AccuseRequestDto.toTargetEntity(requestDto));
    }

    private Accuse findOrCreateAccusation(AccuseRequestDto requestDto, String memberId, AccuseTarget target) {
        return retrieveAccusePort.findByMemberIdAndTarget(memberId, target.getTargetType(), target.getTargetReferenceId())
                .map(existingAccuse -> {
                    existingAccuse.updateReason(requestDto.getReason());
                    return existingAccuse;
                })
                .orElseGet(() -> {
                    target.increaseAccuseCount();
                    return AccuseRequestDto.toEntity(requestDto, memberId, target);
                });
    }
}
