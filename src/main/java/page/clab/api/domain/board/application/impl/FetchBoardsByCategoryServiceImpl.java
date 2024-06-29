package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.FetchBoardsByCategoryService;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchBoardsByCategoryServiceImpl implements FetchBoardsByCategoryService {

    private final MemberLookupService memberLookupService;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public PagedResponseDto<BoardCategoryResponseDto> fetchBoardsByCategory(BoardCategory category, Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Page<Board> boards = boardRepository.findAllByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(board -> BoardCategoryResponseDto.toDto(board, currentMemberInfo, 0L))); // Update the comment count accordingly
    }
}
