package page.clab.api.external.community.board.application.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.mapper.BoardHashtagDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardHashtagPort;
import page.clab.api.domain.community.board.domain.BoardHashtag;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardHashtagUseCase;
import page.clab.api.external.hashtag.application.port.ExternalRetrieveHashtagUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBoardHashtagRetrieveService implements ExternalRetrieveBoardHashtagUseCase {

    private final RetrieveBoardHashtagPort retrieveBoardHashtagPort;
    private final ExternalRetrieveHashtagUseCase externalRetrieveHashtagUseCase;
    private final BoardHashtagDtoMapper boardHashtagDtoMapper;

    @Override
    public List<BoardHashtagResponseDto> getBoardHashtagInfoByBoardId(Long boardId) {
        List<BoardHashtag> boardHashtagList = getAllByBoardId(boardId);
        return boardHashtagList.stream()
                .map(entity -> {
                    String name = externalRetrieveHashtagUseCase.getById(entity.getHashtagId()).getName();
                    return boardHashtagDtoMapper.toDto(entity, name);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardHashtag> getAllByBoardId(Long boardId) {
        return retrieveBoardHashtagPort.getAllByBoardId(boardId);
    }

    @Override
    public List<Long> extractAllHashtagId(List<BoardHashtag> boardHashtagList) {
        return boardHashtagList.stream()
                .map(BoardHashtag::getHashtagId)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardHashtag> getAllIncludingDeletedByBoardId(Long boardId) {
        return retrieveBoardHashtagPort.getAllIncludingDeletedByBoardId(boardId);
    }

    @Override
    public List<Long> getBoardIdsByHashTagId(List<Long> hashtagIds, Pageable pageable) {
        return retrieveBoardHashtagPort.getBoardIdsByHashTagId(hashtagIds, pageable);
    }
}
