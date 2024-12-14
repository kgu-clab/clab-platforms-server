package page.clab.api.domain.community.board.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardHashtagDtoMapper;
import page.clab.api.domain.community.board.application.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.community.board.application.event.BoardUpdatedEvent;
import page.clab.api.domain.community.board.application.port.in.UpdateBoardUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardHashtagPort;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardHashtag;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.board.application.port.ExternalRegisterBoardHashtagUseCase;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardHashtagUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardUpdateService implements UpdateBoardUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RegisterBoardPort registerBoardPort;
    private final RegisterBoardHashtagPort registerBoardHashtagPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveBoardHashtagUseCase externalRetrieveBoardHashtagUseCase;
    private final ExternalRegisterBoardHashtagUseCase externalRegisterBoardHashtagUseCase;
    private final ApplicationEventPublisher eventPublisher;
    private final BoardHashtagDtoMapper boardHashtagDtoMapper;

    @Transactional
    @Override
    public String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.getById(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        updateBoardHashtag(boardId, requestDto.getHashtagIdList(), externalRetrieveBoardHashtagUseCase.getAllIncludingDeletedByBoardId(boardId));
        eventPublisher.publishEvent(new BoardUpdatedEvent(this, board.getId()));
        registerBoardPort.save(board);
        return board.getCategory().getKey();
    }

    @Transactional
    public void updateBoardHashtag(Long boardId, List<Long> newHashtagIds, List<BoardHashtag> currentBoardHashtags) {
        List<Long> currentHashtagIds = externalRetrieveBoardHashtagUseCase.extractAllHashtagId(currentBoardHashtags);
        List<Long> hashtagsToRemove = currentHashtagIds.stream()
                .filter(id -> !newHashtagIds.contains(id))
                .toList();

        List<Long> hashtagsToAdd = newHashtagIds.stream()
                .filter(id -> {
                    // 조건 1: currentHashtagIds에 없는 경우
                    if (!currentHashtagIds.contains(id)) {
                        return true;
                    }
                    // 조건 2: currentBoardHashtags에서 isDeleted=true이고 newHashtagIds에 포함된 경우
                    return currentBoardHashtags.stream()
                            .anyMatch(boardHashtag ->
                                    boardHashtag.getHashtagId().equals(id) &&
                                            boardHashtag.getIsDeleted()
                            );
                })
                .toList();
        hashtagsToRemove.forEach(idToRemove -> {
            currentBoardHashtags.stream()
                    .filter(boardHashtag -> boardHashtag.getHashtagId().equals(idToRemove))
                    .forEach(boardHashtag -> {
                        boardHashtag.toggleIsDeletedStatus();
                        registerBoardHashtagPort.save(boardHashtag);
                    });
        });

        hashtagsToAdd.forEach(idToAdd -> addHashtag(boardId, idToAdd, currentBoardHashtags));
    }

    private void addHashtag(Long boardId, Long hashtagId, List<BoardHashtag> currentBoardHashtags) {
        currentBoardHashtags.stream()
            .filter(boardHashtag -> boardHashtag.getHashtagId().equals(hashtagId) && boardHashtag.getIsDeleted())
            .findFirst()
            .ifPresentOrElse(
                    boardHashtag -> {
                        boardHashtag.toggleIsDeletedStatus();
                        registerBoardHashtagPort.save(boardHashtag);
                    },
                    () -> {
                        externalRegisterBoardHashtagUseCase.registerBoardHashtag(
                                boardHashtagDtoMapper.toDto(boardId, new ArrayList<>(Arrays.asList(hashtagId)))
                        );
                    }
            );
    }
}
