package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.type.dto.RecruitmentRequestDto;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

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
