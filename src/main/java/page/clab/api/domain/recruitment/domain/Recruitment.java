package page.clab.api.domain.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.recruitment.target}")
    private String target;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.recruitment.status}")
    private String status;

    private LocalDateTime updateTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Recruitment of(RecruitmentRequestDto recruitmentRequestDto) {
        return ModelMapperUtil.getModelMapper().map(recruitmentRequestDto, Recruitment.class);
    }

}
