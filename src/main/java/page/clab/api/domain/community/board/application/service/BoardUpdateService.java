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
import page.clab.api.domain.community.board.application.exception.InvalidBoardCategoryHashtagException;
import page.clab.api.domain.community.board.application.port.in.UpdateBoardUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardHashtagPort;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;
import page.clab.api.domain.community.board.domain.BoardHashtag;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.community.board.application.port.in.RegisterBoardHashtagUseCase;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardHashtagUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardUpdateService implements UpdateBoardUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RegisterBoardPort registerBoardPort;
    private final RegisterBoardHashtagPort registerBoardHashtagPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final RetrieveBoardHashtagUseCase retrieveBoardHashtagUseCase;
    private final RegisterBoardHashtagUseCase registerBoardHashtagUseCase;
    private final ApplicationEventPublisher eventPublisher;
    private final BoardHashtagDtoMapper boardHashtagDtoMapper;

    @Transactional
    @Override
    public String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.getById(boardId);
        board.validateAccessPermission(currentMemberInfo);

        board.update(requestDto); // 카테고리 변경된 상태.

        List<Long> hastagIdList = requestDto.getHashtagIdList();
        handleBoardHashtagUpdate(boardId, board, hastagIdList);

        eventPublisher.publishEvent(new BoardUpdatedEvent(this, board.getId()));
        registerBoardPort.save(board);
        return board.getCategory().getKey();
    }

    private void handleBoardHashtagUpdate(Long boardId, Board board, List<Long> hashtagIdList) {
        if (hashtagIdList != null && !hashtagIdList.isEmpty()) {
            board.validateBoardHashtagUpdate();
            updateBoardHashtag(board, hashtagIdList, retrieveBoardHashtagUseCase.getAllIncludingDeletedByBoardId(boardId));
        } else {
            deleteAllHashtagsForBoard(boardId);
        }
    }

    private void deleteAllHashtagsForBoard(Long boardId) {
        List<BoardHashtag> currentBoardHashtags = retrieveBoardHashtagUseCase.getAllIncludingDeletedByBoardId(boardId);
        currentBoardHashtags.forEach(boardHashtag -> {
            if (!boardHashtag.getIsDeleted()) {
                boardHashtag.toggleIsDeletedStatus();
                registerBoardHashtagPort.save(boardHashtag);
            }
        });
    }

    @Transactional
    public void updateBoardHashtag(Board board, List<Long> newHashtagIds, List<BoardHashtag> currentBoardHashtags) {
        validateBoardCategoryForHashtag(board);

        List<Long> currentHashtagIds = getCurrentHashtagIds(currentBoardHashtags);
        List<Long> hashtagsToRemove = findHashtagsToRemove(currentHashtagIds, newHashtagIds);
        List<Long> hashtagsToAdd = findHashtagsToAdd(newHashtagIds, currentHashtagIds, currentBoardHashtags);
        log.info("Current Hashtag IDs: {}", currentHashtagIds);
        log.info("Hashtags to Remove: {}", hashtagsToRemove);
        log.info("Hashtags to Add: {}", hashtagsToAdd);


        removeHashtags(hashtagsToRemove, currentBoardHashtags);
        addHashtags(board.getId(), hashtagsToAdd, currentBoardHashtags);
    }

    private void validateBoardCategoryForHashtag(Board board) {
        board.validateBoardHashtagUpdate();
    }

    private List<Long> getCurrentHashtagIds(List<BoardHashtag> currentBoardHashtags) {
        return retrieveBoardHashtagUseCase.extractAllHashtagId(currentBoardHashtags);
    }

    private List<Long> findHashtagsToRemove(List<Long> currentHashtagIds, List<Long> newHashtagIds) {
        return currentHashtagIds.stream()
                .filter(id -> !newHashtagIds.contains(id))
                .toList();
    }

    private List<Long> findHashtagsToAdd(List<Long> newHashtagIds, List<Long> currentHashtagIds, List<BoardHashtag> currentBoardHashtags) {
        return newHashtagIds.stream()
                .filter(id -> {
                    if (!currentHashtagIds.contains(id)) {
                        return true;
                    }
                    return currentBoardHashtags.stream()
                            .anyMatch(boardHashtag ->
                                    boardHashtag.getHashtagId().equals(id) &&
                                            boardHashtag.getIsDeleted()
                            );
                })
                .toList();
    }

    private void removeHashtags(List<Long> hashtagsToRemove, List<BoardHashtag> currentBoardHashtags) {
        hashtagsToRemove.forEach(idToRemove -> {
            currentBoardHashtags.stream()
                    .filter(boardHashtag -> boardHashtag.getHashtagId().equals(idToRemove))
                    .forEach(boardHashtag -> {
                        if (!boardHashtag.getIsDeleted()) {
                            boardHashtag.toggleIsDeletedStatus();
                        }
                        registerBoardHashtagPort.save(boardHashtag);
                    });
        });
    }

    private void addHashtags(Long boardId, List<Long> hashtagsToAdd, List<BoardHashtag> currentBoardHashtags) {
        hashtagsToAdd.forEach(idToAdd -> addOrUpdateHashtag(boardId, idToAdd, currentBoardHashtags));
    }

    private void addOrUpdateHashtag(Long boardId, Long hashtagId, List<BoardHashtag> currentBoardHashtags) {
        currentBoardHashtags.stream()
                .filter(boardHashtag -> boardHashtag.getHashtagId().equals(hashtagId) && boardHashtag.getIsDeleted())
                .findFirst()
                .ifPresentOrElse(
                        boardHashtag -> {
                            boardHashtag.toggleIsDeletedStatus();
                            registerBoardHashtagPort.save(boardHashtag);
                        },
                        () -> {
                            registerBoardHashtagUseCase.registerBoardHashtag(
                                    boardHashtagDtoMapper.toDto(boardId, List.of(hashtagId))
                            );
                        }
                );
    }
}
