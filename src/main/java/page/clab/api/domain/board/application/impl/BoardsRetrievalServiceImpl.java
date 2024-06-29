package page.clab.api.domain.board.application.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardsRetrievalService;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BoardsRetrievalServiceImpl implements BoardsRetrievalService {

    private final MemberLookupService memberLookupService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieve(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Page<Board> boards = boardRepository.findAll(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = commentRepository.countByBoard(board);
        return BoardListResponseDto.toDto(board, memberInfo, commentCount);
    }
}
