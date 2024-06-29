package page.clab.api.domain.board.application;


import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotNull;
import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardEmojiRepository;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.domain.BoardEmoji;
import page.clab.api.domain.board.domain.SlackBoardInfo;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardEmojiCountResponseDto;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.EmojiUtils;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberLookupService memberLookupService;

    private final NotificationSenderService notificationService;

    private final UploadedFileService uploadedFileService;

    private final ValidationService validationService;

    private final SlackService slackService;

    private final BoardRepository boardRepository;

    private final BoardEmojiRepository boardEmojiRepository;

    private final CommentRepository commentRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardListResponseDto> getBoards(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Page<Board> boards = boardRepository.findAll(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    @Transactional(readOnly = true)
    public BoardDetailsResponseDto getBoardDetails(Long boardId) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Board board = getBoardByIdOrThrow(boardId);
        boolean isOwner = board.isOwner(currentMemberInfo.getMemberId());
        List<BoardEmojiCountResponseDto> boardEmojiCountResponseDtoList = getBoardEmojiCountResponseDtoList(boardId, currentMemberInfo.getMemberId());
        return BoardDetailsResponseDto.toDto(board, currentMemberInfo, isOwner, boardEmojiCountResponseDtoList);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardMyResponseDto> getMyBoards(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberLookupService.getCurrentMemberBasicInfo();
        Page<Board> boards = getBoardByMemberId(pageable, currentMemberInfo.getMemberId());
        return new PagedResponseDto<>(boards.map(board -> BoardMyResponseDto.toDto(board, currentMemberInfo)));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardCategoryResponseDto> getBoardsByCategory(BoardCategory category, Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Page<Board> boards = getBoardByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardCategoryResponseDto(board, currentMemberInfo)));
    }

    @Transactional
    public String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Board board = getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        validationService.checkValid(board);
        return boardRepository.save(board).getCategory().getKey();
    }

    @Transactional
    public String toggleEmojiStatus(Long boardId, String emoji) {
        if (!EmojiUtils.isEmoji(emoji)) {
            throw new InvalidEmojiException("지원하지 않는 이모지입니다.");
        }
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        String memberId = currentMemberInfo.getMemberId();
        Board board = getBoardByIdOrThrow(boardId);
        BoardEmoji boardEmoji = boardEmojiRepository.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
                .map(existingEmoji -> {
                    existingEmoji.toggleIsDeletedStatus();
                    return existingEmoji;
                })
                .orElseGet(() -> BoardEmoji.create(memberId, boardId, emoji));
        boardEmojiRepository.save(boardEmoji);
        return board.getCategory().getKey();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardListResponseDto> getDeletedBoards(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Page<Board> boards = boardRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    public String deleteBoard(Long boardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Board board = getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        boardRepository.delete(board);
        return board.getCategory().getKey();
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = commentRepository.countByBoard(board);
        return BoardListResponseDto.toDto(board, memberInfo, commentCount);
    }

    @NotNull
    private BoardCategoryResponseDto mapToBoardCategoryResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = commentRepository.countByBoard(board);
        return BoardCategoryResponseDto.toDto(board, memberInfo, commentCount);
    }

    public Board getBoardByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    private Page<Board> getBoardByMemberId(Pageable pageable, String memberId) {
        return boardRepository.findAllByMemberId(memberId, pageable);
    }

    private Page<Board> getBoardByCategory(BoardCategory category, Pageable pageable) {
        return boardRepository.findAllByCategory(category, pageable);
    }

    @Transactional(readOnly = true)
    public List<BoardEmojiCountResponseDto> getBoardEmojiCountResponseDtoList(Long boardId, String memberId) {
        List<Tuple> results = boardEmojiRepository.findEmojiClickCountsByBoardId(boardId, memberId);
        return convertToDtoList(results);
    }

    private List<BoardEmojiCountResponseDto> convertToDtoList(List<Tuple> results) {
        return results.stream()
                .map(result -> new BoardEmojiCountResponseDto(
                        result.get("emoji", String.class),
                        result.get("count", Long.class),
                        result.get("isClicked", Boolean.class)))
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanUpOldSoftDeletedRecords() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
        boardEmojiRepository.deleteOldSoftDeletedRecords(cutoffDate);
    }

}