package page.clab.api.domain.community.accuse.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.accuse.adapter.out.persistence.AccuseTargetId;
import page.clab.api.domain.community.accuse.application.dto.mapper.AccuseDtoMapper;
import page.clab.api.domain.community.accuse.application.dto.request.AccuseRequestDto;
import page.clab.api.domain.community.accuse.application.port.in.ReportAccusationUseCase;
import page.clab.api.domain.community.accuse.application.port.out.RegisterAccusePort;
import page.clab.api.domain.community.accuse.application.port.out.RegisterAccuseTargetPort;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccuseTargetPort;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardUseCase;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AccusationReportService implements ReportAccusationUseCase {

    private final RegisterAccusePort registerAccusePort;
    private final RegisterAccuseTargetPort registerAccuseTargetPort;
    private final RetrieveAccusePort retrieveAccusePort;
    private final RetrieveAccuseTargetPort retrieveAccuseTargetPort;
    private final ExternalRetrieveBoardUseCase externalRetrieveBoardUseCase;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final AccuseDtoMapper mapper;

    /**
     * 신고 요청을 처리하고 신고 내역을 저장합니다.
     *
     * <p>신고 유형과 대상을 검사하고, 기존 신고 대상이 있으면 조회하여 신고 횟수를 증가시킵니다.
     * 새로운 신고라면 신고 대상과 내역을 생성하고 저장한 후, 신고자와 관리자를 대상으로 알림을 전송합니다.</p>
     *
     * @param requestDto 신고 요청 정보
     * @return 생성된 신고의 ID
     */
    @Transactional
    @Override
    public Long reportAccusation(AccuseRequestDto requestDto) {
        TargetType type = requestDto.getTargetType();
        Long targetId = requestDto.getTargetId();
        String memberId = externalRetrieveMemberUseCase.getCurrentMemberId();

        validateAccusationRequest(type, targetId, memberId);

        AccuseTarget target = getOrCreateAccuseTarget(requestDto, type, targetId);
        registerAccuseTargetPort.save(target);

        Accuse accuse = findOrCreateAccusation(requestDto, memberId, target);

        externalSendNotificationUseCase.sendNotificationToMember(memberId, "신고하신 내용이 접수되었습니다.");
        externalSendNotificationUseCase.sendNotificationToSuperAdmins(memberId + "님이 신고를 접수하였습니다. 확인해주세요.");
        return registerAccusePort.save(accuse).getId();
    }

    private void validateAccusationRequest(TargetType type, Long targetId, String currentMemberId) {
        switch (type) {
            case BOARD:
                Board board = externalRetrieveBoardUseCase.getById(targetId);
                if (board.isOwner(currentMemberId)) {
                    throw new BaseException(ErrorCode.ACCUSE_TARGET_TYPE_MISMATCH, "자신의 게시글은 신고할 수 없습니다.");
                }
                break;
            case COMMENT:
                Comment comment = externalRetrieveCommentUseCase.getById(targetId);
                if (comment.isOwner(currentMemberId)) {
                    throw new BaseException(ErrorCode.ACCUSE_TARGET_TYPE_MISMATCH, "자신의 댓글은 신고할 수 없습니다.");
                }
                break;
            default:
                throw new BaseException(ErrorCode.ACCUSE_TARGET_TYPE_MISMATCH, "신고 대상 유형이 올바르지 않습니다.");
        }
    }

    private AccuseTarget getOrCreateAccuseTarget(AccuseRequestDto requestDto, TargetType type, Long targetId) {
        return retrieveAccuseTargetPort.findById(AccuseTargetId.create(type, targetId))
            .orElseGet(() -> mapper.fromDto(requestDto));
    }

    private Accuse findOrCreateAccusation(AccuseRequestDto requestDto, String memberId, AccuseTarget target) {
        return retrieveAccusePort.findByMemberIdAndTarget(memberId, target.getTargetType(),
                target.getTargetReferenceId())
            .map(existingAccuse -> {
                existingAccuse.updateReason(requestDto.getReason());
                return existingAccuse;
            })
            .orElseGet(() -> {
                target.increaseAccuseCount();
                return mapper.fromDto(requestDto, memberId, target);
            });
    }
}
