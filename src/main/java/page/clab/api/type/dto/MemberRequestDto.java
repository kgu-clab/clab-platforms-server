package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.StudentStatus;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    private String id;

    private String password;

    private String name;

    private String contact;

    private String email;

    private String department;

    private Long grade;

    private LocalDate birth;

    private String address;

    private StudentStatus studentStatus;

    public static MemberRequestDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberRequestDto.class);
    }

}
