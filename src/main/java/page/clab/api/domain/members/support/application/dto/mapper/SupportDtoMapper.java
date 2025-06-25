package page.clab.api.domain.members.support.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.dto.response.WriterInfo;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.support.application.dto.request.SupportRequestDto;
import page.clab.api.domain.members.support.application.dto.response.AnswerResponseDto;
import page.clab.api.domain.members.support.application.dto.response.SupportDetailsResponseDto;
import page.clab.api.domain.members.support.application.dto.response.SupportListResponseDto;
import page.clab.api.domain.members.support.application.dto.response.SupportMyResponseDto;
import page.clab.api.domain.members.support.application.dto.shared.SupportAnswerInfoDto;
import page.clab.api.domain.members.support.domain.Answer;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.domain.members.support.domain.SupportStatus;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;
import page.clab.api.global.util.RandomNicknameUtil;

import java.util.List;


@Component
@RequiredArgsConstructor
public class SupportDtoMapper {

    private final FileDtoMapper mapper;

    public Support fromDto(SupportRequestDto requestDto, String memberId, List<UploadedFile> uploadedFiles) {
        return Support.builder()
                .memberId(memberId)
                .nickname(RandomNicknameUtil.makeRandomNickname())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .uploadedFiles(uploadedFiles)
                .category(requestDto.getCategory())
                .wantAnonymous(requestDto.isWantAnonymous())
                .status(SupportStatus.PENDING)
                .isDeleted(false)
                .build();
    }

    public SupportListResponseDto toListDto(Support support, MemberDetailedInfoDto memberInfo) {
        WriterInfo writerInfo = create(support, memberInfo);
        return SupportListResponseDto.builder()
                .id(support.getId())
                .title(support.getTitle())
                .writerId(writerInfo.getId())
                .name(writerInfo.getName())
                .createdAt(support.getCreatedAt())
                .status(support.getStatus().getKey())
                .category(support.getCategory().getKey())
                .build();
    }

    public SupportMyResponseDto toDto(Support support, MemberBasicInfoDto memberInfo) {
        return SupportMyResponseDto.builder()
                .id(support.getId())
                .title(support.getTitle())
                .name(support.isWantAnonymous() ? support.getNickname() : memberInfo.getMemberName())
                .category(support.getCategory().getKey())
                .status(support.getStatus().getKey())
                .createdAt(support.getCreatedAt())
                .build();
    }

    public SupportDetailsResponseDto toDto(Support support, MemberDetailedInfoDto memberInfo, boolean isOwner, Answer answer) {
        WriterInfo writerInfo = create(support, memberInfo);
        return SupportDetailsResponseDto.builder()
                .id(support.getId())
                .writerId(writerInfo.getId())
                .name(support.isWantAnonymous() ? support.getNickname() : writerInfo.getName())
                .title(support.getTitle())
                .content(support.getContent())
                .category(support.getCategory().getKey())
                .uploadedFiles(mapper.toDto(support.getUploadedFiles()))
                .isOwner(isOwner)
                .status(support.getStatus().getKey())
                .answer(answer != null ? AnswerResponseDto.from(answer) : null)
                .createdAt(support.getCreatedAt())
                .build();
    }

    public SupportAnswerInfoDto toDto(Support support) {
        return SupportAnswerInfoDto.builder()
                .supportId(support.getId())
                .memberId(support.getMemberId())
                .title(support.getTitle())
                .build();
    }

    public WriterInfo create(Support support, MemberDetailedInfoDto memberInfo) {
        if (support.isWantAnonymous()) {
            return new WriterInfo(null, support.getNickname());
        }
        return new WriterInfo(memberInfo.getMemberId(), memberInfo.getMemberName());
    }
}
