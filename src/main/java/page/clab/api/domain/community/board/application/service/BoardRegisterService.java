package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.request.BoardRequestDto;
import page.clab.api.domain.community.board.application.port.in.RegisterBoardUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.SlackBoardInfo;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardRegisterService implements RegisterBoardUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final SendNotificationUseCase notificationService;
    private final UploadedFileService uploadedFileService;
    private final SlackService slackService;
    private final RegisterBoardPort registerBoardPort;

    @Transactional
    @Override
    public String registerBoard(BoardRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        Board board = BoardRequestDto.toEntity(requestDto, currentMemberInfo.getMemberId(), uploadedFiles);
        board.validateAccessPermissionForCreation(currentMemberInfo);
        if (board.shouldNotifyForNewBoard(currentMemberInfo)) {
            notificationService.sendNotificationToMember(currentMemberInfo.getMemberId(), "[" + board.getTitle() + "] 새로운 공지사항이 등록되었습니다.");
        }
        SlackBoardInfo boardInfo = SlackBoardInfo.create(board, currentMemberInfo);
        slackService.sendNewBoardNotification(boardInfo);
        return registerBoardPort.save(board).getCategory().getKey();
    }
}
