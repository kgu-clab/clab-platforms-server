package page.clab.api.domain.activity.activitygroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Range;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.exception.ActivityGroupNotProgressingException;
import page.clab.api.domain.activity.activitygroup.exception.InvalidGithubUrlException;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;
import java.util.Optional;

@Entity(name = "activity_group")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE activity_group SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ActivityGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityGroupCategory category;

    @Column(nullable = false)
    private String subject;

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

    private String imageUrl;

    @Column(length = 1000)
    @Size(min = 1, max = 1000, message = "{size.activityGroup.curriculum}")
    private String curriculum;

    private LocalDate startDate;

    private LocalDate endDate;

    private String techStack;

    private String githubUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void update(ActivityGroupUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(requestDto.getSubject()).ifPresent(this::setSubject);
        Optional.ofNullable(requestDto.getName()).ifPresent(this::setName);
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(requestDto.getImageUrl()).ifPresent(this::setImageUrl);
        Optional.ofNullable(requestDto.getCurriculum()).ifPresent(this::setCurriculum);
        Optional.ofNullable(requestDto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(requestDto.getEndDate()).ifPresent(this::setEndDate);
        Optional.ofNullable(requestDto.getTechStack()).ifPresent(this::setTechStack);
        Optional.ofNullable(requestDto.getGithubUrl()).ifPresent(this::validateAndSetGithubUrl);
    }

    public boolean isProgressing() {
        return this.status.equals(ActivityGroupStatus.PROGRESSING);
    }

    public boolean isEnded() {
        return this.status.equals(ActivityGroupStatus.END);
    }

    public boolean isSameStatus(ActivityGroupStatus status) {
        return this.status.equals(status);
    }

    public void updateStatus(ActivityGroupStatus status) {
        this.status = status;
    }

    public void updateProgress(Long progress) {
        this.progress = progress;
    }

    public void validateForApplication() {
        if (!this.isProgressing()) {
            throw new ActivityGroupNotProgressingException("해당 활동은 진행중인 활동이 아닙니다.");
        }
    }

    /**
     * GitHub URL을 검증하고 설정합니다.
     * <p>
     * 이 메소드는 다음과 같은 작업을 수행합니다:
     * <ul>
     *   <li>제공된 GitHub URL이 null이거나 빈 문자열인 경우, 아무 작업도 수행하지 않고 메소드를 종료합니다.</li>
     *   <li>URL이 "https://"로 시작하지 않는 경우, "http://"로 시작하는지 확인합니다. "http://"로 시작하면 "https://"로 교정합니다.</li>
     *   <li>URL이 "https://"로 시작하지도 않고, "github.com/"로 시작하는 경우 "https://"를 URL 앞에 붙입니다. 그렇지 않으면 {@link InvalidGithubUrlException} 예외를 발생시킵니다.</li>
     *   <li>URL이 "https://"로 시작하는 경우, URL이 "https://github.com/"로 시작하는지 확인합니다. 그렇지 않으면 {@link InvalidGithubUrlException} 예외를 발생시킵니다.</li>
     *   <li>모든 검증을 통과하면, 잘라낸(trimmed) URL을 {@link ActivityGroup} 객체의 {@code githubUrl} 필드에 설정합니다.</li>
     * </ul>
     *
     * @param githubUrl 검증하고 설정할 GitHub URL. null이거나 빈 값일 수 있습니다.
     * @throws InvalidGithubUrlException 제공된 GitHub URL이 규칙에 맞지 않는 경우 발생합니다.
     */
    public void validateAndSetGithubUrl(String githubUrl) {
        if (githubUrl == null || githubUrl.isEmpty()) {
            return;
        }

        String trimmedUrl = githubUrl.trim();

        if (trimmedUrl.startsWith("http://")) {
            trimmedUrl = "https://" + trimmedUrl.substring(7);
        } else if (!trimmedUrl.startsWith("https://")) {
            if (trimmedUrl.startsWith("github.com/")) {
                trimmedUrl = "https://" + trimmedUrl;
            } else {
                throw new InvalidGithubUrlException("GitHub URL은 'https://github.com/'로 시작해야 합니다.");
            }
        }

        if (!trimmedUrl.startsWith("https://github.com/")) {
            throw new InvalidGithubUrlException("GitHub URL은 'https://github.com/'로 시작해야 합니다.");
        }

        this.githubUrl = trimmedUrl;
    }
}
