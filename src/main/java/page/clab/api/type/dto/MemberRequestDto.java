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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.StudentStatus;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    @NotNull(message = "{notNull.member.id}")
    @Size(min = 9, max = 9, message = "{size.member.id}")
    private String id;

    private String password;

    @NotNull(message = "{notNull.member.name}")
    @Size(min = 1, max = 10, message = "{size.member.name}")
    private String name;

    @NotNull(message = "{notNull.member.contact}")
    @Size(min = 11, max = 11, message = "{size.member.contact}")
    private String contact;

    @NotNull(message = "{notNull.member.email}")
    @Email(message = "{email.member.email}")
    @Size(min = 1, message = "{size.member.email}")
    private String email;

    @NotNull(message = "{notNull.member.department}")
    @Size(min = 1, message = "{size.member.department}")
    private String department;

    @NotNull(message = "{notNull.member.grade}")
    @Min(value = 1, message = "{min.member.grade}")
    @Max(value = 4, message = "{max.member.grade}")
    private Long grade;

    @NotNull(message = "{notNull.member.birth}")
    private LocalDate birth;

    @NotNull(message = "{notNull.member.address}")
    @Size(min = 1, message = "{size.member.address}")
    private String address;

    @NotNull(message = "{notNull.member.studentStatus}")
    private StudentStatus studentStatus;

    @URL(message = "{url.member.imageUrl}")
    private String imageUrl;

    public static MemberRequestDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberRequestDto.class);
    }

}
