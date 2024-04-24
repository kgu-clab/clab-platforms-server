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
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final UploadedFileService uploadedFileService;

    private final ValidationService validationService;

    private final BoardRepository boardRepository;

    private final BoardLikeRepository boardLikeRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public Long createBoard(BoardRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        Board board = BoardRequestDto.toEntity(requestDto, currentMember, uploadedFiles);
        board.validateAccessPermissionForCreation(currentMember);
        validationService.checkValid(board);
        if (board.shouldNotifyForNewBoard()) {
            notificationService.sendNotificationToMember(currentMember, "[" + board.getTitle() + "] 새로운 공지사항이 등록되었습니다.");
        }
        return boardRepository.save(board).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardListResponseDto> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(boards.map(this::mapToBoardListResponseDto));
    }

    @Transactional(readOnly = true)
    public BoardDetailsResponseDto getBoardDetails(Long boardId) {
        Member currentMember = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        boolean hasLikeByMe = checkLikeStatus(board, currentMember);
        boolean isOwner = board.isOwner(currentMember);
        return BoardDetailsResponseDto.toDto(board, hasLikeByMe, isOwner);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardMyResponseDto> getMyBoards(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Board> boards = getBoardByMember(pageable, currentMember);
        return new PagedResponseDto<>(boards.map(BoardMyResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BoardCategoryResponseDto> getBoardsByCategory(BoardCategory category, Pageable pageable) {
        Page<Board> boards = getBoardByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(BoardCategoryResponseDto::toDto));
    }

    @Transactional
    public Long updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMember);
        board.update(requestDto);
        validationService.checkValid(board);
        return boardRepository.save(board).getId();
    }

    @Transactional
    public Long toggleLikeStatus(Long boardId) {
        Member currentMember = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        Optional<BoardLike> boardLikeOpt = boardLikeRepository.findByBoardIdAndMemberId(board.getId(), currentMember.getId());
        if (boardLikeOpt.isPresent()) {
            board.decrementLikes();
            boardLikeRepository.delete(boardLikeOpt.get());
        } else {
            board.incrementLikes();
            BoardLike newBoardLike = BoardLike.create(currentMember.getId(), board.getId());
            validationService.checkValid(newBoardLike);
            boardLikeRepository.save(newBoardLike);
        }
        return board.getLikes();
    }

    public PagedResponseDto<BoardListResponseDto> getDeletedBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(this::mapToBoardListResponseDto));
    }

    public Long deleteBoard(Long boardId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMember);
        boardRepository.delete(board);
        return board.getId();
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board) {
        Long commentCount = commentRepository.countByBoard(board);
        return BoardListResponseDto.toDto(board, commentCount);
    }

    public Board getBoardByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    private Page<Board> getBoardByMember(Pageable pageable, Member member) {
        return boardRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
    }

    private Page<Board> getBoardByCategory(BoardCategory category, Pageable pageable) {
        return boardRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    private boolean checkLikeStatus(Board board, Member member) {
        return boardLikeRepository.existsByBoardIdAndMemberId(board.getId(), member.getId());
    }

}