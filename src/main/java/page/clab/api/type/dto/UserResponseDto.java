package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String id;

    private String name;

    private String contact;

    private String email;

    private String department;

    private Long grade;

    private LocalDate birth;

    private String address;

    private Boolean isInSchool;

    private String imageUrl;

    private Role role;

    private OAuthProvider provider;

    private LocalDateTime createdAt;

}
