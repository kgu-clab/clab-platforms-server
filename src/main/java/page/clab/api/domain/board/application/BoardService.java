package page.clab.api.domain.board.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.board.dao.BoardLikeRepository;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardLike;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final BoardRepository boardRepository;

    private final BoardLikeRepository boardLikeRepository;

    private final FileService fileService;

    @Transactional
    public Long createBoard(BoardRequestDto dto) {
        Member member = memberService.getCurrentMember();
        List<UploadedFile> uploadedFiles = prepareUploadedFiles(dto.getFileUrlList());
        Board board = Board.create(dto, member, uploadedFiles);

        if (board.shouldNotifyForNewBoard()) {
            notificationService.sendNotificationToMember(
                    member,
                    "[" + board.getTitle() + "] 새로운 공지사항이 등록되었습니다."
            );
        }
        return boardRepository.save(board).getId();
    }

    public PagedResponseDto<BoardListResponseDto> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(boards.map(BoardListResponseDto::of));
    }

    public BoardDetailsResponseDto getBoardDetails(Long boardId) {
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        boolean hasLikeByMe = checkLikeStatus(board, member);
        boolean isOwner = board.isOwner(member);
        return BoardDetailsResponseDto.create(board, hasLikeByMe, isOwner);
    }

    public PagedResponseDto<BoardCategoryResponseDto> getMyBoards(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Board> boards = getBoardByMember(pageable, member);
        return new PagedResponseDto<>(boards.map(BoardCategoryResponseDto::of));
    }

    public PagedResponseDto<BoardCategoryResponseDto> getBoardsByCategory(String category, Pageable pageable) {
        Page<Board> boards;
        boards = getBoardByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(BoardCategoryResponseDto::of));
    }

    public Long updateBoard(Long boardId, BoardUpdateRequestDto boardUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        board.checkPermission(member);
        board.update(boardUpdateRequestDto);
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
            BoardLike newBoardLike = new BoardLike(currentMember.getId(), board.getId());
            boardLikeRepository.save(newBoardLike);
        }
        return board.getLikes();
    }

    public Long deleteBoard(Long boardId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        board.checkPermission(currentMember);
        boardRepository.delete(board);
        return board.getId();
    }

    public Board getBoardByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    public boolean isBoardExistById(Long boardId) {
        return boardRepository.existsById(boardId);
    }

    private Page<Board> getBoardByMember(Pageable pageable, Member member) {
        return boardRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
    }

    private Page<Board> getBoardByCategory(String category, Pageable pageable) {
        return boardRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    @NotNull
    private List<UploadedFile> prepareUploadedFiles(List<String> fileUrls) {
        if (fileUrls == null) return new ArrayList<>();
        return fileUrls.stream()
                .map(fileService::getUploadedFileByUrl)
                .collect(Collectors.toList());
    }

    private boolean checkLikeStatus(Board board, Member member) {
        return boardLikeRepository.existsByBoardIdAndMemberId(board.getId(), member.getId());
    }

}