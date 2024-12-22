package page.clab.api.domain.community.board.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.mapper.BoardHashtagDtoMapper;
import page.clab.api.domain.community.board.application.dto.request.BoardRequestDto;
import page.clab.api.domain.community.board.application.port.in.RegisterBoardHashtagUseCase;
import page.clab.api.domain.community.board.application.port.in.RegisterBoardUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.notificationSetting.application.dto.notification.BoardNotificationInfo;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.ExecutivesAlertType;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardRegisterService implements RegisterBoardUseCase {

    private final RegisterBoardPort registerBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final RegisterBoardHashtagUseCase registerBoardHashtagUseCase;
    private final UploadedFileService uploadedFileService;
    private final ApplicationEventPublisher eventPublisher;
    private final BoardDtoMapper boardDtoMapper;
    private final BoardHashtagDtoMapper boardHashtagDtoMapper;

    /**
     * 새로운 게시글을 등록합니다.
     *
     * <p>현재 로그인한 멤버의 정보를 가져와 게시글을 생성하고, 필요한 경우 알림을 전송합니다.
     * 게시글 작성 권한을 검증하며, 공지사항일 경우 Slack과 사용자에게 알림을 보냅니다. 개발 질문일 경우 해시태그의 유무를 확인해 등록합니다.
     * </p>
     *
     * @param requestDto 게시글 요청 정보 DTO
     * @return 등록된 게시글의 카테고리 키
     * @throws PermissionDeniedException 게시글 작성 권한이 없는 경우 예외 발생
     */
    @Transactional
    @Override
    public String registerBoard(BoardRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        Board board = boardDtoMapper.fromDto(requestDto, currentMemberInfo.getMemberId(), uploadedFiles);
        board.validateAccessPermissionForCreation(currentMemberInfo);
        board.validateBoardHashtagRegistration(requestDto.getHashtagIdList());

        if (board.shouldNotifyForNewBoard(currentMemberInfo)) {
            externalSendNotificationUseCase.sendNotificationToMember(currentMemberInfo.getMemberId(),
                "[" + board.getTitle() + "] 새로운 공지사항이 등록되었습니다.");
        }
        BoardNotificationInfo boardInfo = BoardNotificationInfo.create(board, currentMemberInfo);
        eventPublisher.publishEvent(new NotificationEvent(this, ExecutivesAlertType.NEW_BOARD, null,
            boardInfo));

        Board savedBoard = registerBoardPort.save(board);

        if (savedBoard.isDevelopmentQna() && requestDto.getHashtagIdList() != null) {
            registerBoardHashtagUseCase.registerBoardHashtag(
                boardHashtagDtoMapper.toDto(savedBoard.getId(), requestDto.getHashtagIdList()));
        }
        return savedBoard.getCategory().getKey();
    }
}
