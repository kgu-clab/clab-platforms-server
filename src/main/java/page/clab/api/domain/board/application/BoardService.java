package page.clab.api.domain.board.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.RandomNicknameUtil;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final BoardRepository boardRepository;

    private final BoardLikeRepository boardLikeRepository;

    private final RandomNicknameUtil randomNicknameUtil;

    @Transactional
    public Long createBoard(BoardRequestDto boardRequestDto) {
        Member member = memberService.getCurrentMember();
        Board board = Board.of(boardRequestDto);
        board.setMember(member);
        board.setNickName(randomNicknameUtil.makeRandomNickname());
        board.setWantAnonymous(boardRequestDto.isWantAnonymous());
        board.setLikes(0L);
        Long id = boardRepository.save(board).getId();
        if (memberService.isMemberAdminRole(member) && boardRequestDto.getCategory().equals("공지사항")) {
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(member.getId())
                    .content("[" + board.getTitle() + "] 새로운 공지사항이 등록되었습니다.")
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
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        BoardDetailsResponseDto boardDetailsResponseDto = BoardDetailsResponseDto.of(board);
        boardDetailsResponseDto.setHasLikeByMe(boardLikeRepository.existsByBoardIdAndMemberId(board.getId(), member.getId()));
        boardDetailsResponseDto.setOwner(board.getMember().getId().equals(member.getId()));
        return boardDetailsResponseDto;
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
        if (!board.getMember().getId().equals(member.getId())) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        board.update(boardUpdateRequestDto);
        return boardRepository.save(board).getId();
    }

    public Long updateLikes(Long boardId) {
        Member member = memberService.getCurrentMember();
        Board board = getBoardByIdOrThrow(boardId);
        BoardLike boardLike = boardLikeRepository.findByBoardIdAndMemberId(board.getId(), member.getId());

        if (boardLike != null) {
            board.setLikes(Math.min(board.getLikes() - 1, 0));
            boardLikeRepository.delete(boardLike);
        }
        else {
            board.setLikes(board.getLikes() + 1);
            BoardLike newBoardLike = BoardLike.builder()
                    .memberId(member.getId())
                    .boardId(board.getId())
                    .build();
           boardLikeRepository.save(newBoardLike);
        }

        return board.getLikes();
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