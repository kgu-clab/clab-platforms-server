package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.BoardRepository;
import page.clab.api.type.dto.BoardDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void createBoard(BoardDto boardDto, Member member) {
        Board board = Board.of(boardDto);
        board.setWriter(member);
        boardRepository.save(board);
    }

    public BoardDto getBoards(Long boardId) {
        Board board = getBoardById(boardId);
        return BoardDto.of(board);
    }

    public List<BoardDto> getBoardList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto boardDto = BoardDto.of(board);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public List<BoardDto> getMyBoardList(Member member) {
        List<Board> boards = boardRepository.findAllByWriter(member);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto boardDto = BoardDto.of(board);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public List<BoardDto> getBoardListByCategory(String category) {
        List<Board> boards = boardRepository.findAllByCategory(category);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto boardDto = BoardDto.of(board);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public void updateBoard(Member member, Long boardId, BoardDto boardDto) {
        Board board = getBoardById(boardId);
        if (board.getWriter() != member) {
            throw new IllegalArgumentException("작성자만 커뮤니티 글을 수정할 수 있습니다.");
        }
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCategory(boardDto.getCategory());
        board.setWriter(member);
        board.setUpdateTime(LocalDateTime.now());
        boardRepository.save(board);
    }

    public void deleteBoard(Member member, Long boardId) {
        Board board = getBoardById(boardId);
        if (!board.getWriter().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자만 커뮤니티 글을 삭제할 수 있습니다.");
        }
        boardRepository.delete(board);
    }

    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("커뮤니티 글이 존재하지 않습니다."));
    }

}
