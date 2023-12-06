package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BoardRepository;
import page.clab.api.type.dto.BoardCategoryResponseDto;
import page.clab.api.type.dto.BoardDetailsResponseDto;
import page.clab.api.type.dto.BoardListResponseDto;
import page.clab.api.type.dto.BoardRequestDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final BoardRepository boardRepository;

    public Long createBoard(BoardRequestDto boardRequestDto) {
        Member member = memberService.getCurrentMember();
        Board board = Board.of(boardRequestDto);
        board.setMember(member);
        Long id = boardRepository.save(board).getId();

        if (memberService.isMemberAdminRole(member)){
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(member.getId())
                    .content("관리자로부터 " + boardRequestDto.getTitle() + " 게시글이 등록되었습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDto);
        }
        return id;
    }

    public PagedResponseDto<BoardListResponseDto> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(boards.map(BoardListResponseDto::of));
    }

    public BoardDetailsResponseDto getBoardDetails(Long boardId) {
        Board board = getBoardByIdOrThrow(boardId);
        return BoardDetailsResponseDto.of(board);
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

    public Long updateBoard(Long boardId, BoardRequestDto boardRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        if (!board.getMember().getId().equals(member.getId())) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        Board updatedBoard = Board.of(boardRequestDto);
        updatedBoard.setId(board.getId());
        updatedBoard.setMember(board.getMember());
        updatedBoard.setUpdateTime(LocalDateTime.now());
        updatedBoard.setCreatedAt(board.getCreatedAt());
        return boardRepository.save(updatedBoard).getId();
    }

    public Long deleteBoard(Long boardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        if (!(board.getMember().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        boardRepository.delete(board);
        return board.getId();
    }

    public Board getBoardByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    private Page<Board> getBoardByMember(Pageable pageable, Member member) {
        return boardRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
    }

    private Page<Board> getBoardByCategory(String category, Pageable pageable) {
        return boardRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
    }

}