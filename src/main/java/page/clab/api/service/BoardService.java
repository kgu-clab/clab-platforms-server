package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    public List<BoardResonseDto> getBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(BoardResonseDto::of)
                .collect(Collectors.toList());
    }

    public List<BoardResonseDto> getMyBoards() {
        Member member = memberService.getCurrentMember();
        List<Board> boards = boardRepository.findAllByMember(member);
        return boards.stream()
                .map(BoardResonseDto::of)
                .collect(Collectors.toList());
    }

    public List<BoardResonseDto> searchBoards(Long boardId, String category) {
        List<Board> boards = new ArrayList<>();
        if (boardId != null) {
            boards.add(getBoardByIdOrThrow(boardId));
        } else if (category != null) {
            boards.addAll(boardRepository.findAllByCategory(category));
        } else {
            throw new IllegalArgumentException("적어도 boardId, category 중 하나를 제공해야 합니다.");
        }
        if (boards.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return boards.stream()
                .map(BoardResonseDto::of)
                .collect(Collectors.toList());
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

}
