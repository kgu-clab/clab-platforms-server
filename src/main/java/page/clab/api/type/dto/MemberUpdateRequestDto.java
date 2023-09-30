package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberUpdateRequestDto {

    private String password;

    private String name;

    private String contact;

    private String email;

    private String department;

    private Long grade;

    private LocalDate birth;

    private String address;

    private Boolean isInSchool;

    private String imageUrl;

}
