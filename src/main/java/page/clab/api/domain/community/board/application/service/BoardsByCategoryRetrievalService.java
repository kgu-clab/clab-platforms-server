package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardsByCategoryUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;
import page.clab.api.domain.community.comment.application.port.in.RetrieveCommentUseCase;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BoardsByCategoryRetrievalService implements RetrieveBoardsByCategoryUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveCommentUseCase retrieveCommentUseCase;

    @Transactional
    @Override
    public PagedResponseDto<BoardCategoryResponseDto> retrieveBoardsByCategory(BoardCategory category, Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Page<Board> boards = retrieveBoardPort.findAllByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            long commentCount = retrieveCommentUseCase.countCommentsByBoardId(board.getId());
            return  BoardCategoryResponseDto.toDto(board, currentMemberInfo, commentCount);
        }));
    }
}
