package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.mapper.MemberDtoMapper;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberRequestDto;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberContactException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberEmailException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberIdException;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberPasswordUseCase;
import page.clab.api.domain.memberManagement.member.application.port.in.RegisterMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.position.application.port.ExternalRegisterPositionUseCase;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;
import page.clab.api.global.common.email.application.EmailService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberRegisterService implements RegisterMemberUseCase {

    private final CheckMemberExistencePort checkMemberExistencePort;
    private final RegisterMemberPort registerMemberPort;
    private final ManageMemberPasswordUseCase manageMemberPasswordUseCase;
    private final ExternalRegisterPositionUseCase externalRegisterPositionUseCase;
    private final ExternalRetrievePositionUseCase externalRetrievePositionUseCase;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MemberDtoMapper mapper;

    /**
     * 새 멤버를 등록합니다.
     *
     * <p>입력된 회원 정보를 검증하고, 중복되는 회원 정보(ID, 연락처, 이메일)가 없는지 확인합니다.
     * 비밀번호가 입력되지 않은 경우 새 비밀번호를 생성하거나, 입력된 비밀번호를 사용합니다.
     * 등록이 완료되면 기본 직책(Position)을 생성하며, 최종적으로 생성된 계정 정보를
     * 이메일을 통해 사용자에게 전송합니다.</p>
     *
     * @param requestDto 회원 등록 요청 정보를 담은 DTO
     * @return 생성된 멤버의 ID
     * @throws DuplicateMemberIdException 중복된 아이디가 있을 경우 예외 발생
     * @throws DuplicateMemberContactException 중복된 연락처가 있을 경우 예외 발생
     * @throws DuplicateMemberEmailException 중복된 이메일이 있을 경우 예외 발생
     */
    @Transactional
    @Override
    public String registerMember(MemberRequestDto requestDto) {
        checkMemberUniqueness(requestDto);
        Member member = mapper.fromDto(requestDto);
        String finalPassword = manageMemberPasswordUseCase.generateOrRetrievePassword(requestDto.getPassword());
        member.updatePassword(finalPassword, passwordEncoder);
        registerMemberPort.save(member);
        createPositionByMember(member);
        emailService.sendAccountCreationEmail(member, finalPassword);
        return member.getId();
    }

    private void checkMemberUniqueness(MemberRequestDto requestDto) {
        if (checkMemberExistencePort.existsById(requestDto.getId()))
            throw new DuplicateMemberIdException("이미 사용 중인 아이디입니다.");
        if (checkMemberExistencePort.existsByContact(requestDto.getContact()))
            throw new DuplicateMemberContactException("이미 사용 중인 연락처입니다.");
        if (checkMemberExistencePort.existsByEmail(requestDto.getEmail()))
            throw new DuplicateMemberEmailException("이미 사용 중인 이메일입니다.");
    }

    public void createPositionByMember(Member member) {
        if (externalRetrievePositionUseCase.findByMemberIdAndYearAndPositionType(member.getId(), String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.create(member.getId());
        externalRegisterPositionUseCase.save(position);
    }
}
