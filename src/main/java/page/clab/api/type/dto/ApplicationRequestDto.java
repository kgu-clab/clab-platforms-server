package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import page.clab.api.type.entity.Application;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApplicationRequestDto {

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

    private static ModelMapper modelMapper;

    public static ApplicationRequestDto of(Application application) {
        return modelMapper.map(application, ApplicationRequestDto.class);
    }

}
