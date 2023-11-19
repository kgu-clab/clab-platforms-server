package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.BoardRepository;
import page.clab.api.type.dto.BoardRequestDto;
import page.clab.api.type.dto.BoardResonseDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberService memberService;

    private final BoardRepository boardRepository;

    public void createBoard(BoardRequestDto boardRequestDto) {
        Member member = memberService.getCurrentMember();
        Board board = Board.of(boardRequestDto);
        board.setMember(member);
        boardRepository.save(board);
    }

    public List<BoardResonseDto> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return boards.map(BoardResonseDto::of).getContent();
    }

    public List<BoardResonseDto> getMyBoards(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Board> boards = getBoardByMember(pageable, member);
        return boards.map(BoardResonseDto::of).getContent();
    }

    public List<BoardResonseDto> searchBoards(Long boardId, String category, Pageable pageable) {
        Page<Board> boards;
        if (boardId != null) {
            Board board = getBoardByIdOrThrow(boardId);
            boards = new PageImpl<>(Arrays.asList(board), pageable, 1);
        } else if (category != null) {
            boards = getBoardByCategory(category, pageable);
        } else {
            throw new IllegalArgumentException("적어도 boardId, category 중 하나를 제공해야 합니다.");
        }
        if (boards.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return boards.map(BoardResonseDto::of).getContent();
    }

    public void updateBoard(Long boardId, BoardRequestDto boardRequestDto) throws PermissionDeniedException {
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
        boardRepository.save(updatedBoard);
    }

    public void deleteBoard(Long boardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        if (!(board.getMember().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        boardRepository.delete(board);
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
