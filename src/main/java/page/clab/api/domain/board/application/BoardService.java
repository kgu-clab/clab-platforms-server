package page.clab.api.domain.board.application;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardLikeRepository;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.domain.BoardLike;
import page.clab.api.domain.board.domain.SlackBoardInfo;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberLookupService memberLookupService;

    private final NotificationService notificationService;

    private final UploadedFileService uploadedFileService;

    private final ValidationService validationService;

    private final SlackService slackService;

    private final BoardRepository boardRepository;

    private final BoardLikeRepository boardLikeRepository;

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
        boolean hasLikeByMe = checkLikeStatus(board, currentMemberInfo);
        boolean isOwner = board.isOwner(currentMemberInfo.getMemberId());
        return BoardDetailsResponseDto.toDto(board, currentMemberInfo, hasLikeByMe, isOwner);
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
    public Long toggleLikeStatus(Long boardId) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Board board = getBoardByIdOrThrow(boardId);
        Optional<BoardLike> boardLikeOpt = boardLikeRepository.findByBoardIdAndMemberId(board.getId(), currentMemberId);
        if (boardLikeOpt.isPresent()) {
            board.decrementLikes();
            boardLikeRepository.delete(boardLikeOpt.get());
        } else {
            board.incrementLikes();
            BoardLike newBoardLike = BoardLike.create(currentMemberId, board.getId());
            validationService.checkValid(newBoardLike);
            boardLikeRepository.save(newBoardLike);
        }
        return board.getLikes();
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

    private boolean checkLikeStatus(Board board, MemberDetailedInfoDto memberInfo) {
        return boardLikeRepository.existsByBoardIdAndMemberId(board.getId(), memberInfo.getMemberId());
    }

}