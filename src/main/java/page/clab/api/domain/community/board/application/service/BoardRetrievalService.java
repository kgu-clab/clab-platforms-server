package page.clab.api.domain.community.board.application.service;

import com.drew.lang.annotations.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardHashtagUseCase;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PaginationUtils;

@Service
@RequiredArgsConstructor
public class BoardRetrievalService implements RetrieveBoardUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final RetrieveBoardHashtagUseCase retrieveBoardHashtagUseCase;
    private final BoardDtoMapper mapper;

    @Transactional
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieveBoards(Pageable pageable) {
        if (!BoardSortableFields.canSortInDatabase(pageable)) {
            return retrieveBoardsSortedInMemory(pageable);
        }
        Page<Board> boards = retrieveBoardPort.findAll(pageable);
        return new PagedResponseDto<>(boards.map(this::toDto));
    }

    private PagedResponseDto<BoardListResponseDto> retrieveBoardsSortedInMemory(Pageable pageable) {
        List<Board> boards = retrieveBoardPort.findAll();
        List<BoardListResponseDto> boardListResponseDtos = boards.stream().map(this::toDto).toList();
        List<BoardListResponseDto> paginatedBoardListResponseDtos =
            PaginationUtils.applySortingAndSlicing(boardListResponseDtos, pageable);
        return new PagedResponseDto<>(paginatedBoardListResponseDtos, boards.size(), pageable);
    }

    @Override
    public Board getById(Long boardId) {
        return retrieveBoardPort.getById(boardId);
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }

    @NotNull
    private BoardListResponseDto toDto(Board board) {
        MemberDetailedInfoDto memberInfo = getMemberDetailedInfoByBoard(board);
        Long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());
        List<BoardHashtagResponseDto> boardHashtagInfos = retrieveBoardHashtagUseCase.getBoardHashtagInfoByBoardId(
            board.getId());
        return mapper.toListDto(board, memberInfo, commentCount, boardHashtagInfos);
    }
}
