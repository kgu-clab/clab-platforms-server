package page.clab.api.domain.accuse.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.dao.AccuseTargetRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.dto.response.AccuseMemberInfo;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.domain.accuse.exception.AccuseTargetTypeIncorrectException;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.comment.application.CommentService;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.review.application.ReviewService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccuseService {

    private final MemberService memberService;

    private final BoardService boardService;

    private final CommentService commentService;

    private final ReviewService reviewService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final AccuseRepository accuseRepository;

    private final AccuseTargetRepository accuseTargetRepository;

    private Map<TargetType, Function<Long, Boolean>> typeValidationMap;

    @Transactional
    public Long createAccuse(AccuseRequestDto requestDto) {
        TargetType type = requestDto.getTargetType();
        Long targetId = requestDto.getTargetId();
        validateAccuseRequest(type, targetId);

        AccuseTarget target = getOrCreateAccuseTarget(requestDto, type, targetId);
        validationService.checkValid(target);
        accuseTargetRepository.save(target);

        Member currentMember = memberService.getCurrentMember();
        Accuse accuse = findOrCreateAccuse(requestDto, currentMember, target);
        validationService.checkValid(accuse);

        notificationService.sendNotificationToMember(currentMember, "신고하신 내용이 접수되었습니다.");
        notificationService.sendNotificationToSuperAdmins(currentMember.getName() + "님이 신고를 접수하였습니다. 확인해주세요.");
        return accuseRepository.save(accuse).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AccuseResponseDto> getAccusesByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        Page<AccuseTarget> accuseTargets = accuseTargetRepository.findByConditions(type, status, countOrder, pageable);
        List<AccuseResponseDto> responseDtos = accuseTargets.stream()
                .map(accuseTarget -> {
                    List<Accuse> accuses = accuseRepository.findByTargetOrderByCreatedAtDesc(accuseTarget);
                    List<AccuseMemberInfo> members = AccuseMemberInfo.create(accuses);
                    return AccuseResponseDto.toDto(accuses.getFirst(), members);
                })
                .toList();
        return new PagedResponseDto<>(responseDtos, pageable, responseDtos.size());
    }

    @Transactional
    public Long updateAccuseStatus(Long accuseId, AccuseStatus status) {
        Accuse accuse = getAccuseByIdOrThrow(accuseId);
        accuse.getTarget().updateStatus(status);
        validationService.checkValid(accuse);
        notificationService.sendNotificationToMember(accuse.getMember(), "신고 상태가 " + status.getDescription() + "로 변경되었습니다.");
        return accuseRepository.save(accuse).getId();
    }

    private Accuse getAccuseByIdOrThrow(Long accuseId) {
        return accuseRepository.findById(accuseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고입니다."));
    }

    @PostConstruct
    public void init() {
        typeValidationMap = Map.of(
                TargetType.BOARD, boardService::isBoardExistById,
                TargetType.COMMENT, commentService::isCommentExistById,
                TargetType.REVIEW, reviewService::isReviewExistsById
        );
    }

    private void validateAccuseRequest(TargetType type, Long targetId) {
        Function<Long, Boolean> validationFunction = typeValidationMap.get(type);
        if (validationFunction == null) {
            throw new AccuseTargetTypeIncorrectException("신고 대상 유형이 올바르지 않습니다.");
        }
        if (!validationFunction.apply(targetId)) {
            throw new NotFoundException(type.getDescription() + " ID " + targetId + "을 찾을 수 없습니다.");
        }
    }

    private AccuseTarget getOrCreateAccuseTarget(AccuseRequestDto requestDto, TargetType type, Long targetId) {
        return accuseTargetRepository.findById(AccuseTargetId.create(type, targetId))
                .orElseGet(() -> AccuseRequestDto.toTargetEntity(requestDto));
    }

    private Accuse findOrCreateAccuse(AccuseRequestDto requestDto, Member currentMember, AccuseTarget target) {
        return accuseRepository.findByMemberAndTarget(currentMember, target)
                .map(existingAccuse -> {
                    existingAccuse.updateReason(requestDto.getReason());
                    return existingAccuse;
                })
                .orElseGet(() -> {
                    target.increaseAccuseCount();
                    return AccuseRequestDto.toEntity(requestDto, currentMember, target);
                });
    }

}
