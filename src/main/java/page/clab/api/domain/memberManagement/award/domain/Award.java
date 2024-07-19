package page.clab.api.domain.memberManagement.award.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.award.application.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Award {

    private Long id;
    private String competitionName;
    private String organizer;
    private String awardName;
    private LocalDate awardDate;
    private String memberId;
    private Boolean isDeleted;

    public void update(AwardUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getCompetitionName()).ifPresent(this::setCompetitionName);
        Optional.ofNullable(requestDto.getOrganizer()).ifPresent(this::setOrganizer);
        Optional.ofNullable(requestDto.getAwardName()).ifPresent(this::setAwardName);
        Optional.ofNullable(requestDto.getAwardDate()).ifPresent(this::setAwardDate);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new PermissionDeniedException("해당 게시글을 수정/삭제할 권한이 없습니다.");
        }
    }
}
