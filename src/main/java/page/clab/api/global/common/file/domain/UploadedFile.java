package page.clab.api.global.common.file.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member uploader;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String saveFileName;

    @Column(nullable = false, unique = true)
    private String savedPath;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String category;

    private Long storagePeriod;

    public static UploadedFile create(Member uploader, String originalFileName, String saveFileName, String savedPath, String url, Long fileSize, String contentType, Long storagePeriod, String category) {
        return UploadedFile.builder()
                .uploader(uploader)
                .originalFileName(originalFileName)
                .saveFileName(saveFileName)
                .savedPath(savedPath)
                .url(url)
                .fileSize(fileSize)
                .contentType(contentType)
                .storagePeriod(storagePeriod)
                .category(category)
                .build();
    }

    public boolean isOwner(Member member) {
        return this.uploader.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isSuperAdminRole()) {
            throw new PermissionDeniedException("해당 파일을 삭제할 권한이 없습니다.");
        }
    }

}
