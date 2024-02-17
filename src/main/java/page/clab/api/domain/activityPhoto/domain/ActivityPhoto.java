package page.clab.api.domain.activityPhoto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.global.common.file.domain.UploadedFile;
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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "files")
    private List<UploadedFile> uploadedFiles = new ArrayList<>();

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