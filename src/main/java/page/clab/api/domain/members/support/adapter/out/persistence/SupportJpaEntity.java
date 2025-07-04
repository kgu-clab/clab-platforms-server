package page.clab.api.domain.members.support.adapter.out.persistence;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.members.support.domain.SupportCategory;
import page.clab.api.domain.members.support.domain.SupportStatus;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.util.List;

@Entity
@Table(name = "support")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE support SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class SupportJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id",nullable = false)
    private String memberId;

    private String nickname;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.support.title}")
    private String title;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.support.content}")
    private String content;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_files")
    private List<UploadedFile> uploadedFiles;

    @Column(nullable = false)
    private boolean wantAnonymous;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SupportCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SupportStatus status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
