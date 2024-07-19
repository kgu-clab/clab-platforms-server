package page.clab.api.domain.members.activityPhoto.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityPhoto extends BaseEntity {

    private Long id;
    private String title;
    private List<UploadedFile> uploadedFiles;
    private LocalDate date;
    private Boolean isPublic;
    private Boolean isDeleted;

    public void delete() {
        this.isDeleted = true;
    }

    public void togglePublicStatus() {
        this.isPublic = !this.isPublic;
    }
}
