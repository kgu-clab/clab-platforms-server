package page.clab.api.domain.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE recruitment SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.recruitment.target}")
    private String target;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    public void update(RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        Optional.ofNullable(recruitmentUpdateRequestDto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getEndDate()).ifPresent(this::setEndDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getApplicationType()).ifPresent(this::setApplicationType);
        Optional.ofNullable(recruitmentUpdateRequestDto.getTarget()).ifPresent(this::setTarget);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateStatus(RecruitmentStatus status) { this.status = status; }

}
