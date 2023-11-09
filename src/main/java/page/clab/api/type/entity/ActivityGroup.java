package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.dto.ActivityGroupDto;
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
    private String category;

    @Column(name = "name", nullable = false)
    @Size(max = 30)
    private String name;

    @Column(nullable = false)
    @Size(max = 1000)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupStatus status;

    @Column(nullable = false)
    @Size(max = 100, min=0)
    private Long progress;

    @Column(nullable = true)
    private String code;

    @URL
    @Column(name = "image_url", nullable = true)
    private String imageUrl;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static ActivityGroup of(ActivityGroupDto activityGroupDto) {
        return ModelMapperUtil.getModelMapper().map(activityGroupDto, ActivityGroup.class);
    }

}