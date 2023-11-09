package page.clab.api.type.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.etc.StudentStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberUpdateRequestDto {

    @NotNull
    @Size(min = 1, max = 10)
    private String name;

    @NotNull
    @Size(min = 11, max = 11)
    private String contact;

    @NotNull
    @Email
    @Unique
    private String email;

    @NotNull
    private String department;

    @NotNull
    @Min(1)
    @Max(4)
    private Long grade;

    @NotNull
    private LocalDate birth;

    @NotNull
    private String address;

    @NotNull
    private StudentStatus studentStatus;

    @URL
    private String imageUrl;

}
