package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotProgressingException;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.util.Optional;

@Entity(name = "activity_group")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupCategory category;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    @Size(min = 1, max = 30, message = "{size.activityGroup.name}")
    private String name;

    @Column(nullable = false, length = 1000)
    @Size(min = 1, max = 1000, message = "{size.activityGroup.content}")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupStatus status;

    @Range(min = 0, max = 100, message = "{range.activityGroup.progress}")
    private Long progress;

    private String imageUrl;

    private String curriculum;

    private LocalDate startDate;

    private LocalDate endDate;

    private String techStack;

    @URL(message = "{url.activityGroup.githubUrl}")
    private String githubUrl;

    public static ActivityGroup create(ActivityGroupRequestDto activityGroupRequestDto) {
        ActivityGroup activityGroup = ModelMapperUtil.getModelMapper().map(activityGroupRequestDto, ActivityGroup.class);
        activityGroup.setStatus(ActivityGroupStatus.WAITING);
        activityGroup.setProgress(0L);
        return activityGroup;
    }

    public void update(ActivityGroupUpdateRequestDto dto) {
        Optional.ofNullable(dto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(dto.getSubject()).ifPresent(this::setSubject);
        Optional.ofNullable(dto.getName()).ifPresent(this::setName);
        Optional.ofNullable(dto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(dto.getImageUrl()).ifPresent(this::setImageUrl);
        Optional.ofNullable(dto.getCurriculum()).ifPresent(this::setCurriculum);
        Optional.ofNullable(dto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(dto.getEndDate()).ifPresent(this::setEndDate);
        Optional.ofNullable(dto.getTechStack()).ifPresent(this::setTechStack);
        Optional.ofNullable(dto.getGithubUrl()).ifPresent(this::setGithubUrl);
    }

    public boolean isWaiting() {
        return this.status.equals(ActivityGroupStatus.WAITING);
    }

    public boolean isProgressing() {
        return this.status.equals(ActivityGroupStatus.PROGRESSING);
    }

    public boolean isEnded() {
        return this.status.equals(ActivityGroupStatus.END);
    }

    public void updateStatus(ActivityGroupStatus status) {
        this.status = status;
    }

    public boolean isStudy() {
        return this.category.equals(ActivityGroupCategory.STUDY);
    }

    public boolean isProject() {
        return this.category.equals(ActivityGroupCategory.PROJECT);
    }

    public void validateForApplication() {
        if (!this.isProgressing()) {
            throw new ActivityGroupNotProgressingException("해당 활동은 진행중인 활동이 아닙니다.");
        }
    }

}