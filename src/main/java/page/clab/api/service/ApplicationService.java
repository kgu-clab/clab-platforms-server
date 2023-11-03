package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.auth.util.AuthUtil;
import page.clab.api.exception.AlreadyApprovedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.repository.MemberRepository;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.entity.Application;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.Role;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final MemberService memberService;

    private final LoginFailInfoService loginFailInfoService;

    private final ApplicationRepository applicationRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void createApplication(ApplicationRequestDto appRequestDto) {
        Application application = Application.of(appRequestDto);
        application.setContact(memberService.removeHyphensFromContact(application.getContact()));
        applicationRepository.save(application);
    }

    public List<ApplicationResponseDto> getApplications() throws PermissionDeniedException {
        checkMemberAdminRole();
        List<Application> applications = applicationRepository.findAll();
        List<ApplicationResponseDto> appRequestDtos = new ArrayList<>();
        for (Application application : applications) {
            ApplicationResponseDto appRequestDto = createApplicationResponseDto(application);
            appRequestDtos.add(appRequestDto);
        }
        return appRequestDtos;
    }

    public List<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        checkMemberAdminRole();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Application> applicationsBetweenDates = applicationRepository.findApplicationsBetweenDates(startDateTime, endDateTime);
        List<ApplicationResponseDto> appRequestDtos = new ArrayList<>();
        for (Application application : applicationsBetweenDates) {
            ApplicationResponseDto appRequestDto = createApplicationResponseDto(application);
            appRequestDtos.add(appRequestDto);
        }
        return appRequestDtos;
    }

    @Transactional
    public List<ApplicationResponseDto> getApprovedApplications() throws PermissionDeniedException {
        checkMemberAdminRole();
        List<Application> applications = applicationRepository.findAll();
        return applications.stream()
                .map(this::createApplicationResponseDto)
                .filter(ApplicationResponseDto::isPass)
                .collect(Collectors.toList());
    }

    public ApplicationResponseDto searchApplication(String applicationId, String name) throws PermissionDeniedException {
        checkMemberAdminRole();
        Application application = null;
        if (applicationId != null)
            application = getApplicationByIdOrThrow(applicationId);
        else if (name != null)
            application = getApplicationByNameOrThrow(name);
        else
            throw new IllegalArgumentException("적어도 applicationId 또는 name 중 하나를 제공해야 합니다.");

        if (application == null)
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return createApplicationResponseDto(application);
    }

    @Transactional
    public void approveApplication(String applicationId) throws PermissionDeniedException {
        checkMemberAdminRole();
        if (memberRepository.existsById(applicationId))
            throw new AlreadyApprovedException("이미 승인된 신청자입니다.");
        Application application = getApplicationByIdOrThrow(applicationId);
        Member approvedMember = Member.of(application);
        approvedMember.setPassword(passwordEncoder.encode(approvedMember.getPassword()));
        memberRepository.save(approvedMember);
        loginFailInfoService.createLoginFailInfo(approvedMember);
    }

    @Transactional
    public void cancelApplication(String applicationId) throws PermissionDeniedException {
        checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        Member approvedMember = memberService.getMemberByIdOrThrow(applicationId);
        if (approvedMember.getCreatedAt().isBefore(LocalDateTime.now().minusDays(1)))
            throw new UnAuthorizeException("취소할 수 없는 신청입니다.");
        loginFailInfoService.deleteLoginFailInfo(applicationId);
        memberRepository.delete(approvedMember);
    }

    public void deleteApplication(String applicationId) throws PermissionDeniedException {
        checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        applicationRepository.delete(application);
    }

    private void checkMemberAdminRole() throws PermissionDeniedException {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = memberRepository.findById(memberId).get();
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

    public Application getApplicationByIdOrThrow(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
    }

    public Application getApplicationByNameOrThrow(String name) {
        return applicationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
    }

    private ApplicationResponseDto createApplicationResponseDto(Application application) {
        ApplicationResponseDto appRequestDto = ApplicationResponseDto.of(application);
        if (memberRepository.findById(application.getStudentId()).isPresent())
            appRequestDto.setPass(true);
        return appRequestDto;
    }

}
