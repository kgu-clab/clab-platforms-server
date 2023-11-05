package page.clab.api.type.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import page.clab.api.type.dto.MemberRequestDto;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.dto.MemberUpdateRequestDto;
import page.clab.api.type.etc.MemberStatus;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;
import page.clab.api.type.etc.StudentStatus;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
public class Member implements UserDetails {

    @Id
    @Column(updatable = false, unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long grade;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDate birth;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String imageUrl;

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

    public static Member of(MemberResponseDto memberResponseDto) {
        return ModelMapperUtil.getModelMapper().map(memberResponseDto, Member.class);
    }

    public static Member of(MemberUpdateRequestDto memberUpdateRequestDto) {
        return ModelMapperUtil.getModelMapper().map(memberUpdateRequestDto, Member.class);
    }

    public static Member of(Application application) {
        Member member = ModelMapperUtil.getModelMapper().map(application, Member.class);
        member.setPassword(member.generatePassword(member.getBirth()));
        member.setStudentStatus(StudentStatus.CURRENT);
        member.setRole(Role.USER);
        member.setProvider(OAuthProvider.LOCAL);
        return member;
    }

    public String generatePassword(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("생년월일이 올바르지 않습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return birthDate.format(formatter);
    }
  
}
