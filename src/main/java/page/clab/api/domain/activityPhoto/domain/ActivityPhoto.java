package page.clab.api.domain.activityPhoto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE activity_photo SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ActivityPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_photo_files")
    private List<UploadedFile> uploadedFiles;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean isPublic;

    public void delete() {
        this.isDeleted = true;
    }

    public void togglePublicStatus() {
        this.isPublic = !this.isPublic;
    }
}