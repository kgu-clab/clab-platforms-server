package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.dto.WorkExperienceRequestDto;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.workExperience.companyName}")
    private String companyName;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.workExperience.position}")
    private String position;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne
    private Member member;

    public static WorkExperience of(WorkExperienceRequestDto workExperienceRequestDto) {
        return ModelMapperUtil.getModelMapper().map(workExperienceRequestDto, WorkExperience.class);
    }

}
