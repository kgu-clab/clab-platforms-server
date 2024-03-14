package page.clab.api.domain.application.application;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.domain.application.domain.QApplication;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.recruitment.application.RecruitmentService;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final MemberService memberService;

    private final RecruitmentService recruitmentService;

    private final NotificationService notificationService;

    private final SlackService slackService;

    private final ApplicationRepository applicationRepository;

    private final EntityManager entityManager;

    @Transactional
    public String createApplication(HttpServletRequest request, @Valid ApplicationRequestDto applicationRequestDto) {
        Application application = Application.of(applicationRequestDto);
        Recruitment recruitment = recruitmentService.getRecruitmentByIdOrThrow(applicationRequestDto.getRecruitmentId());
        application.setContact(memberService.removeHyphensFromContact(application.getContact()));
        application.setIsPass(false);
        application.setUpdateTime(LocalDateTime.now());
        String id = applicationRepository.save(application).getStudentId();
        notificationService.sendNotificationToAdmins(
                applicationRequestDto.getStudentId() + " " +
                        applicationRequestDto.getName() + "님이 동아리에 지원하였습니다."
        );
        slackService.sendApplicationNotification(request, applicationRequestDto);
        return id;
    }

    public PagedResponseDto<ApplicationResponseDto> getApplicationsByCondition(String recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QApplication application = QApplication.application;

        BooleanBuilder builder = new BooleanBuilder();

        if (recruitmentId != null && !recruitmentId.isEmpty()) {
            builder.and(application.recruitmentId.eq(Long.valueOf(recruitmentId)));
        }
        if (studentId != null && !studentId.isEmpty()) {
            builder.and(application.studentId.eq(studentId));
        }
        if (isPass != null) {
            builder.and(application.isPass.eq(isPass));
        }

        List<Application> applications = queryFactory.selectFrom(application)
                .where(builder.getValue())
                .orderBy(application.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = (int) queryFactory.from(application)
                .where(builder.getValue())
                .fetchCount();

        List<ApplicationResponseDto> responseDtos = applications.stream()
                .map(ApplicationResponseDto::of)
                .collect(Collectors.toList());

        return new PagedResponseDto<>(responseDtos, pageable, total);
    }

    public String approveApplication(Long recruitmentId, String applicationId) {
        Application application = getApplicationByIdOrThrow(applicationId, recruitmentId);
        application.setIsPass(!application.getIsPass());
        application.setUpdateTime(LocalDateTime.now());
        return applicationRepository.save(application).getStudentId();
    }

    public ApplicationPassResponseDto getApplicationPass(Long recruitmentId, String applicationId) {
        Application application = getApplicationById(applicationId, recruitmentId);
        if (application == null) {
            return ApplicationPassResponseDto.builder()
                    .isPass(false)
                    .build();
        }
        return ApplicationPassResponseDto.of(application);
    }

    public String deleteApplication(Long recruitmentId, String applicationId) {
        Application application = getApplicationByIdOrThrow(applicationId, recruitmentId);
        applicationRepository.delete(application);
        return application.getStudentId();
    }

    private Application getApplicationByIdOrThrow(String applicationId, Long recruitmentId) {
        ApplicationId id = new ApplicationId(applicationId, recruitmentId);
        return applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 지원자가 없습니다."));
    }

    private Application getApplicationById(String applicationId, Long recruitmentId) {
        ApplicationId id = new ApplicationId(applicationId, recruitmentId);
        return applicationRepository.findById(id)
                .orElse(null);
    }

}
