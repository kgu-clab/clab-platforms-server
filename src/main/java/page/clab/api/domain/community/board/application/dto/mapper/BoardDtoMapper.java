package page.clab.api.domain.community.board.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.dto.request.BoardRequestDto;
import page.clab.api.domain.community.board.application.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardEmojiCountResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardMyResponseDto;
import page.clab.api.domain.community.board.application.dto.response.WriterInfo;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;
import page.clab.api.global.util.RandomNicknameUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardDtoMapper {

    private final FileDtoMapper dtoMapper;

    public Board fromDto(BoardRequestDto requestDto, String memberId, List<UploadedFile> uploadedFiles) {
        return Board.builder()
                .memberId(memberId)
                .nickname(RandomNicknameUtil.makeRandomNickname())
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .uploadedFiles(uploadedFiles)
                .imageUrl(requestDto.getImageUrl())
                .wantAnonymous(requestDto.isWantAnonymous())
                .isDeleted(false)
                .build();
    }

    public BoardDetailsResponseDto toDto(Board board, MemberDetailedInfoDto memberInfo, boolean isOwner, List<BoardEmojiCountResponseDto> emojiInfos) {
        WriterInfo writerInfo = createDetailWriterInfo(board, memberInfo);
        return BoardDetailsResponseDto.builder()
                .id(board.getId())
                .writerId(writerInfo.getId())
                .writerName(writerInfo.getName())
                .writerRoleLevel(writerInfo.getRoleLevel())
                .writerImageUrl(writerInfo.getImageUrl())
                .category(board.getCategory().getKey())
                .title(board.getTitle())
                .content(board.getContent())
                .files(dtoMapper.toDto(board.getUploadedFiles()))
                .imageUrl(board.getImageUrl())
                .isOwner(isOwner)
                .emojiInfos(emojiInfos)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardMyResponseDto toDto(Board board, MemberBasicInfoDto memberInfo) {
        return BoardMyResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory().getKey())
                .writerName(board.isWantAnonymous() ? board.getNickname() : memberInfo.getMemberName())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardCommentInfoDto toDto(Board board) {
        return BoardCommentInfoDto.builder()
                .boardId(board.getId())
                .memberId(board.getMemberId())
                .title(board.getTitle())
                .category(board.getCategory())
                .build();
    }

    public BoardCategoryResponseDto toBoardCategoryResponseDto(Board board, MemberDetailedInfoDto memberInfo, Long commentCount) {
        WriterInfo writerInfo = createWriterInfo(board, memberInfo);
        return BoardCategoryResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory().getKey())
                .writerId(writerInfo.getId())
                .writerName(writerInfo.getName())
                .title(board.getTitle())
                .commentCount(commentCount)
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardListResponseDto toBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo, Long commentCount) {
        WriterInfo writerInfo = createWriterInfo(board, memberInfo);
        return BoardListResponseDto.builder()
                .id(board.getId())
                .writerId(writerInfo.getId())
                .writerName(writerInfo.getName())
                .category(board.getCategory().getKey())
                .title(board.getTitle())
                .content(board.getContent())
                .commentCount(commentCount)
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public WriterInfo createWriterInfo(Board board, MemberDetailedInfoDto memberInfo) {
        if (memberInfo.isAdminRole() && board.isNotice()) {
            return new WriterInfo(null, "운영진");
        } else if (board.isWantAnonymous()) {
            return new WriterInfo(null, board.getNickname());
        }
        return new WriterInfo(memberInfo.getMemberId(), memberInfo.getMemberName());
    }

    public WriterInfo createDetailWriterInfo(Board board, MemberDetailedInfoDto memberInfo) {
        if (memberInfo.isAdminRole() && board.isNotice()) {
            return new WriterInfo(null, "운영진", memberInfo.getRoleLevel(), null);
        } else if (board.isWantAnonymous()) {
            return new WriterInfo(null, board.getNickname(), null, null);
        }
        return new WriterInfo(memberInfo.getMemberId(), memberInfo.getMemberName(), memberInfo.getRoleLevel(), memberInfo.getImageUrl());
    }
}
