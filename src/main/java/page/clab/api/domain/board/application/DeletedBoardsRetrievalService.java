package page.clab.api.domain.board.application;

import com.drew.lang.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.RetrieveDeletedBoardsUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveDeletedBoardsPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.comment.application.port.out.CountCommentsByBoardPort;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBoardsRetrievalService implements RetrieveDeletedBoardsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveDeletedBoardsPort retrieveDeletedBoardsPort;
    private final CountCommentsByBoardPort countCommentsByBoardPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieve(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Page<Board> boards = retrieveDeletedBoardsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = countCommentsByBoardPort.countByBoard(board);
        return BoardListResponseDto.toDto(board, memberInfo, commentCount);
    }
}
