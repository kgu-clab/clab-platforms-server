package page.clab.api.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity implements UserDetails {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @Size(min = 9, max = 9, message = "{size.member.id}")
    private String id;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.member.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 9, max = 11, message = "{size.member.contact}")
    private String contact;

    @Column(nullable = false)
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
    private String interests;

    @URL(message = "{url.member.githubUrl}")
    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

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

    private Member(String id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public static Member createUserDetails(Member member) {
        return new Member(member.getId(), member.getPassword(), member.getRole());
    }

    public void update(MemberUpdateRequestDto memberUpdateRequestDto, PasswordEncoder passwordEncoder) {
        Optional.ofNullable(memberUpdateRequestDto.getPassword())
                .ifPresent(password -> setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(memberUpdateRequestDto.getContact()).ifPresent(this::setContact);
        Optional.ofNullable(memberUpdateRequestDto.getEmail()).ifPresent(this::setEmail);
        Optional.ofNullable(memberUpdateRequestDto.getGrade()).ifPresent(this::setGrade);
        Optional.ofNullable(memberUpdateRequestDto.getBirth()).ifPresent(this::setBirth);
        Optional.ofNullable(memberUpdateRequestDto.getAddress()).ifPresent(this::setAddress);
        Optional.ofNullable(memberUpdateRequestDto.getInterests()).ifPresent(this::setInterests);
        Optional.ofNullable(memberUpdateRequestDto.getGithubUrl()).ifPresent(this::setGithubUrl);
        Optional.ofNullable(memberUpdateRequestDto.getStudentStatus()).ifPresent(this::setStudentStatus);
        Optional.ofNullable(memberUpdateRequestDto.getImageUrl()).ifPresent(this::setImageUrl);
    }

    public boolean isAdminRole() {
        return role.equals(Role.ADMIN) || role.equals(Role.SUPER);
    }

    public boolean isSuperAdminRole() {
        return role.equals(Role.SUPER);
    }

    public boolean isSameMember(Member member) {
        return id.equals(member.getId());
    }

    public boolean isSameMember(String memberId) {
        return id.equals(memberId);
    }

    public boolean isSameName(String memberName) {
        return name.equals(memberName);
    }

    public boolean isSameEmail(String memberEmail) {
        return email.equals(memberEmail);
    }

    public boolean isOwner(Member member) {
        return this.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isSuperAdminRole()) {
            throw new PermissionDeniedException("해당 멤버를 수정/삭제할 권한이 없습니다.");
        }
    }

    public void validateAccessPermissionForCloud(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isSuperAdminRole()) {
            throw new PermissionDeniedException("해당 멤버의 클라우드 사용량을 조회할 권한이 없습니다.");
        }
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        setPassword(passwordEncoder.encode(password));
    }

    public void updateLastLoginTime() {
        lastLoginTime = LocalDateTime.now();
    }

    public void checkLoanSuspension() {
        if (loanSuspensionDate != null && LocalDateTime.now().isBefore(loanSuspensionDate)) {
            throw new LoanSuspensionException("대출 정지 중입니다. 대출 정지일까지는 책을 대출할 수 없습니다.");
        }
    }

    public void handleOverdueAndSuspension(long overdueDays) {
        if (overdueDays > 0) {
            LocalDateTime currentDate = LocalDateTime.now();
            if (loanSuspensionDate == null || loanSuspensionDate.isBefore(currentDate)) {
                loanSuspensionDate = LocalDateTime.now().plusDays(overdueDays * 7);;
            } else {
                loanSuspensionDate = loanSuspensionDate.plusDays(overdueDays * 7);
            }
        }
    }

}
