package page.clab.api.domain.activityPhoto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @URL(message = "{url.activityPhoto.imageUrl}")
    private String imageUrl;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean isPublic;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static ActivityPhoto of(ActivityPhotoRequestDto activityPhotoRequestDto) {
        ActivityPhoto activityPhoto = ModelMapperUtil.getModelMapper().map(activityPhotoRequestDto, ActivityPhoto.class);
        activityPhoto.isPublic = false;
        return activityPhoto;
    }

}