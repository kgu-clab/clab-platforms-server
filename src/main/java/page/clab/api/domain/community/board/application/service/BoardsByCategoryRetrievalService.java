package page.clab.api.domain.community.board.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardOverviewResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardHashtagUseCase;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardsByCategoryUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PaginationUtils;

@Service
@RequiredArgsConstructor
public class BoardsByCategoryRetrievalService implements RetrieveBoardsByCategoryUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final RetrieveBoardHashtagUseCase retrieveBoardHashtagUseCase;
    private final BoardDtoMapper mapper;

    @Transactional
    @Override
    public PagedResponseDto<BoardOverviewResponseDto> retrieveBoardsByCategory(BoardCategory category,
        Pageable pageable) {
        if (!BoardSortableFields.canSortInDatabase(pageable)) {
            return retrieveBoardsByCategorySortedInMemory(category, pageable);
        }
        Page<Board> boards = retrieveBoardPort.findAllByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(this::toDto));
    }

    private PagedResponseDto<BoardOverviewResponseDto> retrieveBoardsByCategorySortedInMemory(
        BoardCategory category, Pageable pageable) {
        List<Board> boards = retrieveBoardPort.findAllByCategory(category);
        List<BoardOverviewResponseDto> boardOverviewResponseDtos = boards.stream().map(this::toDto).toList();
        List<BoardOverviewResponseDto> paginatedBoardOverviewDtos =
            PaginationUtils.applySortingAndSlicing(boardOverviewResponseDtos, pageable);
        return new PagedResponseDto<>(paginatedBoardOverviewDtos, boards.size(), pageable);
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }

    private BoardOverviewResponseDto toDto(Board board) {
        long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());
        List<BoardHashtagResponseDto> boardHashtagInfos = retrieveBoardHashtagUseCase.getBoardHashtagInfoByBoardId(
            board.getId());
        return mapper.toCategoryDto(board, getMemberDetailedInfoByBoard(board), commentCount, boardHashtagInfos);
    }
}
