package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGroupBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupBoardCategory category;

    @Size(min = 1, max = 100, message = "{size.board.title}")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ActivityGroupBoard parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<ActivityGroupBoard> children = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "files")
    private List<UploadedFile> uploadedFiles = new ArrayList<>();

    @Column(name = "dueDate_time")
    private LocalDateTime dueDateTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static ActivityGroupBoard of(ActivityGroupBoardRequestDto activityGroupBoardRequestDto) {
        return ModelMapperUtil.getModelMapper().map(activityGroupBoardRequestDto, ActivityGroupBoard.class);
    }

    public void update(ActivityGroupBoardUpdateRequestDto dto, FileService fileService) {
        Optional.ofNullable(dto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(dto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(dto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(dto.getDueDateTime()).ifPresent(this::setDueDateTime);
        Optional.ofNullable(dto.getFileUrls())
                .ifPresent(urls -> {
                    List<UploadedFile> uploadedFiles = urls.stream()
                            .map(fileService::getUploadedFileByUrl)
                            .collect(Collectors.toList());
                    setUploadedFiles(uploadedFiles);
                });
    }

}
