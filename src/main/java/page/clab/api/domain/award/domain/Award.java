package page.clab.api.domain.award.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Award extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "{size.award.competitionName}")
    private String competitionName;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "{size.award.organizer}")
    private String organizer;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "{size.award.awardName}")
    private String awardName;

    @Column(nullable = false)
    private LocalDate awardDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(AwardUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getCompetitionName()).ifPresent(this::setCompetitionName);
        Optional.ofNullable(requestDto.getOrganizer()).ifPresent(this::setOrganizer);
        Optional.ofNullable(requestDto.getAwardName()).ifPresent(this::setAwardName);
        Optional.ofNullable(requestDto.getAwardDate()).ifPresent(this::setAwardDate);
    }

    public boolean isOwner(Member member) {
        return this.member.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 게시글을 수정/삭제할 권한이 없습니다.");
        }
    }

}
