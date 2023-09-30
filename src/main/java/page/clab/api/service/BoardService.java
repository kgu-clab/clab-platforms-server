package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.BoardRepository;
import page.clab.api.type.dto.BoardDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.Role;
import page.clab.api.type.vo.BoardVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void createBoard(Member member, BoardDto boardDto) {
        if(member.getRole() != Role.USER){
            throw new IllegalArgumentException("로그인을 해야 커뮤니티 글을 작성할 수 있습니다.");
        }
        Board board = Board.of(boardDto);
        boardRepository.save(board);
    }

    public BoardVo getBoards(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (!board.isPresent()) {
            throw new NotFoundException("존재하지 않는 커뮤니티 글입니다.");
        }
        return new BoardVo(board.get());
    }

    public List<BoardVo> getBoardList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardVo> boardVos = new ArrayList<>();
        for (Board board : boards) {
            BoardVo boardVo = new BoardVo(board);
            boardVos.add(boardVo);
        }
        return boardVos;
    }

    public List<BoardVo> getBoardList(Member member) {
        List<Board> boards = boardRepository.findAllByMemberId(member.getId());
        List<BoardVo> boardVos = new ArrayList<>();
        for (Board board : boards) {
            BoardVo boardVo = new BoardVo(board);
            boardVos.add(boardVo);
        }
        return boardVos;
    }

    public List<BoardVo> getBoardList(String c) {
        String category;
        if (c.equals("질문")){
            category = "질문";
        } else if (c.equals("졸업")){
            category = "졸업";
        } else {
            throw new IllegalArgumentException("올바르지 않은 카테고리입니다.");
        }
        List<Board> boards = boardRepository.findAllByCategory(category);
        List<BoardVo> boardVos = new ArrayList<>();
        for (Board board : boards) {
            BoardVo boardVo = new BoardVo(board);
            boardVos.add(boardVo);
        }
        return boardVos;
    }

    public void updateBoard(Member member, Long boardId, BoardDto boardDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("존재하지 않는 커뮤니티 글입니다."));
        if (!board.getWriter().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자만 커뮤니티 글을 수정할 수 있습니다.");
        }
        board.update(boardDto);
        boardRepository.save(board);
    }

    public void deleteBoard(Member member, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("존재하지 않는 커뮤니티 글입니다."));
        if (!board.getWriter().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자만 커뮤니티 글을 삭제할 수 있습니다.");
        }
        boardRepository.delete(board);
    }

}
