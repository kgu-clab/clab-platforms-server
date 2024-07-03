package page.clab.api.domain.board.application;

import com.drew.lang.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.BoardsRetrievalUseCase;
import page.clab.api.domain.board.application.port.out.LoadBoardPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardsPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.comment.application.port.out.CountCommentsByBoardPort;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BoardsRetrievalService implements BoardsRetrievalUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RetrieveBoardsPort retrieveBoardsPort;
    private final CountCommentsByBoardPort countCommentsByBoardPort;
    private final LoadBoardPort loadBoardPort;

    @Transactional
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieve(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Page<Board> boards = retrieveBoardsPort.findAll(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    @Override
    public Board findByIdOrThrow(Long boardId) {
        return loadBoardPort.findByIdOrThrow(boardId);
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = countCommentsByBoardPort.countByBoard(board);
        return BoardListResponseDto.toDto(board, memberInfo, commentCount);
    }
}
