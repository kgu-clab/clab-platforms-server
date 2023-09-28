package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Application;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponseDto {

    private String studentId;

    private String name;

    private String contact;

    private String email;

    private String department;

    private Long grade;

    private LocalDate birth;

    private String address;

    private String interests;

    private String otherActivities;

    private boolean isPass;

    private LocalDateTime createdAt;

    public static ApplicationResponseDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationResponseDto.class);
    }

}
