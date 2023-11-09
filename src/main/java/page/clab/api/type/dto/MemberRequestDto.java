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
import org.checkerframework.common.aliasing.qual.Unique;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.StudentStatus;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    @NotNull
    private String id;

    private String password;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 11, max = 11)
    private String contact;

    @NotNull
    @Unique
    @Email
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

    public static MemberRequestDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberRequestDto.class);
    }

}
