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
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Award {

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

    public static Award create(AwardRequestDto awardRequestDto, Member member) {
        Award award = ModelMapperUtil.getModelMapper().map(awardRequestDto, Award.class);
        award.setMember(member);
        return award;
    }

    public void update(AwardUpdateRequestDto awardUpdateRequestDto) {
        Optional.ofNullable(awardUpdateRequestDto.getCompetitionName()).ifPresent(this::setCompetitionName);
        Optional.ofNullable(awardUpdateRequestDto.getOrganizer()).ifPresent(this::setOrganizer);
        Optional.ofNullable(awardUpdateRequestDto.getAwardName()).ifPresent(this::setAwardName);
        Optional.ofNullable(awardUpdateRequestDto.getAwardDate()).ifPresent(this::setAwardDate);
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
