package page.clab.api.domain.community.board.application.service;

import com.drew.lang.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveDeletedBoardsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBoardsRetrievalService implements RetrieveDeletedBoardsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveCommentPort retrieveCommentPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieveDeletedBoards(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Page<Board> boards = retrieveBoardPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = retrieveCommentPort.countByBoardId(board.getId());
        return BoardListResponseDto.toDto(board, memberInfo, commentCount);
    }
}
