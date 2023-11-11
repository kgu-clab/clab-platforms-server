package page.clab.api.type.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import page.clab.api.type.dto.MemberRequestDto;
import page.clab.api.type.etc.MemberStatus;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;
import page.clab.api.type.etc.StudentStatus;
import page.clab.api.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @Column(updatable = false, unique = true, nullable = false)
    @Size(min = 9, max = 9, message = "{size.member.id}")
    private String id;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.member.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 11, max = 11, message = "{size.member.contact}")
    private String contact;

    @Column(unique = true, nullable = false)
    @Email(message = "{email.member.email}")
    @Size(min = 1, message = "{size.member.email}")
    private String email;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.member.department}")
    private String department;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.member.grade}")
    @Max(value = 4, message = "{max.member.grade}")
    private Long grade;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.member.address}")
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    @URL(message = "{url.member.imageUrl}")
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginTime;

    private LocalDateTime loanSuspensionDate;

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

    public static Member of(MemberRequestDto memberRequestDto) {
        Member member = ModelMapperUtil.getModelMapper().map(memberRequestDto, Member.class);
        member.setMemberStatus(MemberStatus.ACTIVE);
        member.setRole(Role.USER);
        member.setProvider(OAuthProvider.LOCAL);
        return member;
    }
  
}
