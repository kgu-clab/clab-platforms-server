package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.dto.ActivityGroupRequestDto;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "activity_group")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupCategory category;

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

    @URL(message = "{url.activityGroup.imageUrl}")
    private String imageUrl;

    private String curriculum;

    private LocalDate startDate;

    private LocalDate endDate;

    private String techStack;

    @URL(message = "{url.activityGroup.githubUrl}")
    private String githubUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static ActivityGroup of(ActivityGroupRequestDto activityGroupRequestDto) {
        return ModelMapperUtil.getModelMapper().map(activityGroupRequestDto, ActivityGroup.class);
    }

}