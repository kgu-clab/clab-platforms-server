package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Component
public class MemberMapper {

    public MemberJpaEntity toJpaEntity(Member member) {
        return MemberJpaEntity.builder()
                .id(member.getId())
                .password(member.getPassword())
                .name(member.getName())
                .contact(member.getContact())
                .email(member.getEmail())
                .department(member.getDepartment())
                .grade(member.getGrade())
                .birth(member.getBirth())
                .address(member.getAddress())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .studentStatus(member.getStudentStatus())
                .imageUrl(member.getImageUrl())
                .role(member.getRole())
                .lastLoginTime(member.getLastLoginTime())
                .loanSuspensionDate(member.getLoanSuspensionDate())
                .isOtpEnabled(member.getIsOtpEnabled())
                .isDeleted(member.isDeleted())
                .build();
    }

    public Member toDomainEntity(MemberJpaEntity jpaEntity) {
        return Member.builder()
                .id(jpaEntity.getId())
                .password(jpaEntity.getPassword())
                .name(jpaEntity.getName())
                .contact(jpaEntity.getContact())
                .email(jpaEntity.getEmail())
                .department(jpaEntity.getDepartment())
                .grade(jpaEntity.getGrade())
                .birth(jpaEntity.getBirth())
                .address(jpaEntity.getAddress())
                .interests(jpaEntity.getInterests())
                .githubUrl(jpaEntity.getGithubUrl())
                .studentStatus(jpaEntity.getStudentStatus())
                .imageUrl(jpaEntity.getImageUrl())
                .role(jpaEntity.getRole())
                .lastLoginTime(jpaEntity.getLastLoginTime())
                .loanSuspensionDate(jpaEntity.getLoanSuspensionDate())
                .isOtpEnabled(jpaEntity.getIsOtpEnabled())
                .isDeleted(jpaEntity.isDeleted())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
