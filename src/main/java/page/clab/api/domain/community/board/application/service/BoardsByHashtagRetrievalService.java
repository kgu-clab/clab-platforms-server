package page.clab.api.domain.community.board.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardOverviewResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardsByHashtagUseCase;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardHashtagUseCase;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardUseCase;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.hashtag.application.port.ExternalRetrieveHashtagUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PaginationUtils;

@Service
@RequiredArgsConstructor
public class BoardsByHashtagRetrievalService implements RetrieveBoardsByHashtagUseCase {

    private final ExternalRetrieveHashtagUseCase externalRetrieveHashtagUseCase;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final RetrieveBoardHashtagUseCase retrieveBoardHashtagUseCase;
    private final ExternalRetrieveBoardUseCase externalRetrieveBoardUseCase;
    private final BoardDtoMapper mapper;

    @Override
    public PagedResponseDto<BoardOverviewResponseDto> retrieveBoardsByHashtag(List<String> hashtags, Pageable pageable) {
        List<Long> hashtagIds = new ArrayList<>();
        for (String hashtag : hashtags) {
            hashtagIds.add(externalRetrieveHashtagUseCase.getByName(hashtag).getId());
        }

        List<Long> boardIds = retrieveBoardHashtagUseCase.getBoardIdsByHashTagId(hashtagIds);

        List<Board> boards = boardIds.stream()
                .map(externalRetrieveBoardUseCase::getById)
                .toList();

        List<BoardOverviewResponseDto> boardOverviewResponseDtos =
                boards.stream().map(board -> {
                    long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());
                    List<BoardHashtagResponseDto> boardHashtagInfos = retrieveBoardHashtagUseCase.getBoardHashtagInfoByBoardId(board.getId());
                    return mapper.toCategoryDto(board, getMemberDetailedInfoByBoard(board), commentCount, boardHashtagInfos);
                }).toList();

        List<BoardOverviewResponseDto> paginatedBoardOverviewDtos = PaginationUtils.applySortingAndSlicing(boardOverviewResponseDtos, pageable);

        return new PagedResponseDto<>(paginatedBoardOverviewDtos, boardIds.size(), pageable);
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }
}
