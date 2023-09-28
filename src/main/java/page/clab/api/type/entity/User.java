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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(
            unique = true,
            nullable = false,
            length = 100
    )
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

}
