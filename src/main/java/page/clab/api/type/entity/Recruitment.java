package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.type.dto.RecruitmentRequestDto;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

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

    private String period;

    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    private String target;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Recruitment of(RecruitmentRequestDto recruitmentRequestDto) {
        return ModelMapperUtil.getModelMapper().map(recruitmentRequestDto, Recruitment.class);
    }

}
