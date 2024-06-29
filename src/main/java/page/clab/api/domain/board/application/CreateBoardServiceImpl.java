package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.SlackBoardInfo;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateBoardServiceImpl implements CreateBoardService {

    private final MemberLookupService memberLookupService;
    private final NotificationSenderService notificationService;
    private final UploadedFileService uploadedFileService;
    private final ValidationService validationService;
    private final SlackService slackService;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public String createBoard(BoardRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        Board board = BoardRequestDto.toEntity(requestDto, currentMemberInfo.getMemberId(), uploadedFiles);
        board.validateAccessPermissionForCreation(currentMemberInfo);
        validationService.checkValid(board);
        if (board.shouldNotifyForNewBoard(currentMemberInfo)) {
            notificationService.sendNotificationToMember(currentMemberInfo.getMemberId(), "[" + board.getTitle() + "] 새로운 공지사항이 등록되었습니다.");
        }
        SlackBoardInfo boardInfo = SlackBoardInfo.create(board, currentMemberInfo);
        slackService.sendNewBoardNotification(boardInfo);
        return boardRepository.save(board).getCategory().getKey();
    }
}
