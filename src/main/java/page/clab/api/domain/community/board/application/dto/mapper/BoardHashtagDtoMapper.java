package page.clab.api.domain.community.board.application.dto.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.dto.request.BoardHashtagRequestDto;
import page.clab.api.domain.community.board.domain.BoardHashtag;

@Component
@RequiredArgsConstructor
public class BoardHashtagDtoMapper {

    public BoardHashtag fromDto(Long boardId, Long hashtagId){
        return BoardHashtag.builder()
                .boardId(boardId)
                .hashtagId(hashtagId)
                .isDeleted(false)
                .build();
    }

    public BoardHashtagRequestDto toDto(Long boardId, List<Long> hashtagIdList) {
        return BoardHashtagRequestDto.builder()
                .boardId(boardId)
                .hashtagIdList(hashtagIdList)
                .build();
    }
}
