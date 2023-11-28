package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.dto.ActivityPhotoRequestDto;
import page.clab.api.util.ModelMapperUtil;

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
    @URL(message = "{url.activityPhoto.imageUrl}")
    private String imageUrl;

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