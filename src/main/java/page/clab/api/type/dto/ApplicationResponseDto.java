package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import page.clab.api.type.entity.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
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

    private static ModelMapper modelMapper;

    public static ApplicationResponseDto of(Application application) {
        return modelMapper.map(application, ApplicationResponseDto.class);
    }

}
