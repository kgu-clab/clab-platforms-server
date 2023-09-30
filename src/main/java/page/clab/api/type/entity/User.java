package page.clab.api.type.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import page.clab.api.type.dto.UserRequestDto;
import page.clab.api.type.dto.UserResponseDto;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="\"user\"")
public class User implements UserDetails {

    @Id
    @Column(updatable = false, unique = true, nullable = false)
    @Size(min = 9, max = 9)
    private String id;

    @Column(nullable = false)
    @Size(min = 6)
    @JsonIgnore
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole().getKey()));
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User of(UserRequestDto userRequestDto) {
        return ModelMapperUtil.getModelMapper().map(userRequestDto, User.class);
    }

    public static User of(UserResponseDto userResponseDto) {
        return ModelMapperUtil.getModelMapper().map(userResponseDto, User.class);
    }

    public static User of(Application application) {
        User user = ModelMapperUtil.getModelMapper().map(application, User.class);
        user.setPassword(user.generatePassword(user.getBirth()));
        user.setIsInSchool(true);
        user.setRole(Role.USER);
        user.setProvider(OAuthProvider.LOCAL);
        return user;
    }

    public String generatePassword(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("생년월일이 올바르지 않습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return birthDate.format(formatter);
    }
  
}
