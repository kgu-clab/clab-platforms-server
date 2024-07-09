package page.clab.api.domain.member.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.domain.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.member.application.port.out.RemoveMemberPort;
import page.clab.api.domain.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.member.application.port.out.UpdateMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
        CheckMemberExistencePort,
        RegisterMemberPort,
        UpdateMemberPort,
        RemoveMemberPort,
        RetrieveMemberPort {

    private final MemberRepository memberRepository;

    @Override
    public boolean existsById(String id) {
        return memberRepository.existsById(id);
    }

    @Override
    public boolean existsByContact(String contact) {
        return memberRepository.existsByContact(contact);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void delete(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public Member update(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Page<Member> findByConditions(String id, String name, Pageable pageable) {
        return memberRepository.findByConditions(id, name, pageable);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Member findByIdOrThrow(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("[Member] id: " + memberId + "에 해당하는 회원이 존재하지 않습니다."));
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        return memberRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Member findByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("[Member] email: " + email + "을 사용하는 회원이 존재하지 않습니다."));
    }

    @Override
    public Page<Member> findBirthdaysThisMonth(int month, Pageable pageable) {
        return memberRepository.findBirthdaysThisMonth(month, pageable);
    }
}
