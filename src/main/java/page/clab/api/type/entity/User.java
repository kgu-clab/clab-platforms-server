package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Size(min = 9, max = 9)
    private String id;

    @Column(nullable = false)
    @Size(min = 6)
    private String password;

    private String uid;

    @Column(nullable = false)
    @Size(max = 10)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    @Min(1)
    @Max(4)
    private Long grade;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDate birth;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean isInSchool;

    @URL(message = "유효한 URL 값이 아닙니다.")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // UserService로 빠질 예정
    public static User toUser(Application application) {
        User user = User.builder()
                .id(application.getStudentId())
                .password(generatePassword(application.getBirth()))
                .name(application.getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .department(application.getDepartment())
                .grade(application.getGrade())
                .birth(application.getBirth())
                .address(application.getAddress())
                .isInSchool(true)
                .role(Role.USER)
                .provider(OAuthProvider.LOCAL)
                .build();
        return user;
    }

    // UserService로 빠질 예정
    public static String generatePassword(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("생년월일이 올바르지 않습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return birthDate.format(formatter);
    }

}
