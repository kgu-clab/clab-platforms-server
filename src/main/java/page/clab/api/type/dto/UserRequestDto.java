package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import page.clab.api.type.entity.User;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String id;

    private String password;

    private String name;

    private String contact;

    private String email;

    private String department;

    private Long grade;

    private LocalDate birth;

    private String address;

    private Boolean isInSchool;

    private static ModelMapper modelMapper;

    public static UserRequestDto of(User user) {
        return modelMapper.map(user, UserRequestDto.class);
    }

}
