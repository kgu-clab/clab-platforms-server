package page.clab.api.domain.memberManagement.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member implements UserDetails {

    private String id;
    private String password;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;
    private String githubUrl;
    private StudentStatus studentStatus;
    private String imageUrl;
    private Role role;
    private LocalDateTime lastLoginTime;
    private LocalDateTime loanSuspensionDate;
    private Boolean isOtpEnabled;

    @Builder.Default
    private boolean isDeleted = false;
    private LocalDateTime createdAt;

    private Member(String id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public static Member createUserDetails(Member member) {
        return new Member(member.getId(), member.getPassword(), member.getRole());
    }

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

    public void update(MemberUpdateRequestDto requestDto, PasswordEncoder passwordEncoder) {
        Optional.ofNullable(requestDto.getPassword())
                .ifPresent(password -> setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(requestDto.getContact()).ifPresent(this::setContact);
        Optional.ofNullable(requestDto.getEmail()).ifPresent(this::setEmail);
        Optional.ofNullable(requestDto.getGrade()).ifPresent(this::setGrade);
        Optional.ofNullable(requestDto.getBirth()).ifPresent(this::setBirth);
        Optional.ofNullable(requestDto.getAddress()).ifPresent(this::setAddress);
        Optional.ofNullable(requestDto.getInterests()).ifPresent(this::setInterests);
        Optional.ofNullable(requestDto.getGithubUrl()).ifPresent(this::setGithubUrl);
        Optional.ofNullable(requestDto.getStudentStatus()).ifPresent(this::setStudentStatus);
        Optional.ofNullable(requestDto.getImageUrl()).ifPresent(this::setImageUrl);
        if (isAdminRole()) {
            isOtpEnabled = true;
        } else {
            Optional.ofNullable(requestDto.getIsOtpEnabled()).ifPresent(this::setIsOtpEnabled);
        }
    }

    public void delete() {
        this.isDeleted = true;
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

    public boolean isGraduated() {
        return studentStatus.equals(StudentStatus.GRADUATED);
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

    public void updateLoanSuspensionDate(LocalDateTime loanSuspensionDate) {
        this.loanSuspensionDate = loanSuspensionDate;
    }

    public void clearImageUrl() {
        this.imageUrl = null;
    }
}
