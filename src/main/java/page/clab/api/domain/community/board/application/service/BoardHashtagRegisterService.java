package page.clab.api.domain.community.board.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.mapper.BoardHashtagDtoMapper;
import page.clab.api.domain.community.board.application.dto.request.BoardHashtagRequestDto;
import page.clab.api.domain.community.board.application.port.in.RegisterBoardHashtagUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardHashtagPort;
import page.clab.api.domain.community.board.domain.BoardHashtag;
import page.clab.api.domain.community.hashtag.domain.Hashtag;
import page.clab.api.external.hashtag.application.port.ExternalRetrieveHashtagUseCase;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BoardHashtagRegisterService implements RegisterBoardHashtagUseCase {

    private final RegisterBoardHashtagPort registerBoardHashtagPort;
    private final ExternalRetrieveHashtagUseCase externalRetrieveHashtagUseCase;
    private final BoardHashtagDtoMapper mapper;

    @Transactional
    @Override
    public Long registerBoardHashtag(BoardHashtagRequestDto requestDto) {
        Long boardId = requestDto.getBoardId();
        for (Long hashtagId : requestDto.getHashtagIdList()) {
            Hashtag hashtag = externalRetrieveHashtagUseCase.getById(hashtagId);
            BoardHashtag boardHashtag = mapper.fromDto(boardId, hashtagId);
            registerBoardHashtagPort.save(boardHashtag);
        }
        return boardId;
    }
}
