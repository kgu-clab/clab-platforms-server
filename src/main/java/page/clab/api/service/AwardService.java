package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.AwardRepository;
import page.clab.api.type.dto.AwardRequestDto;
import page.clab.api.type.dto.AwardResponseDto;
import page.clab.api.type.entity.Award;
import page.clab.api.type.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final MemberService memberService;

    private final AwardRepository awardRepository;

    private final EntityManager entityManager;

    public void createAward(AwardRequestDto awardRequestDto) {
        Award award = Award.of(awardRequestDto);
        awardRepository.save(award);
    }

    public List<AwardResponseDto> getAwards() {
        List<Award> awards = awardRepository.findAll();
        return awards.stream()
                .map(AwardResponseDto::of)
                .toList();
    }

    @Transactional
    public List<AwardResponseDto> searchAwards(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Award> criteriaQuery = criteriaBuilder.createQuery(Award.class);
        Root<Award> root = criteriaQuery.from(Award.class);
        Predicate predicate = criteriaBuilder.or(
                criteriaBuilder.like(root.get("competitionName"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("organizer"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("awardName"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("participants"), "%" + keyword + "%")
        );
        criteriaQuery.select(root).where(predicate);
        List<Award> matchingAwards = entityManager.createQuery(criteriaQuery).getResultList();
        return matchingAwards.stream()
                .map(AwardResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateAward(Long awardId, AwardRequestDto awardRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!(award.getParticipants().contains(member.getName()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 수상 이력을 수정할 권한이 없습니다.");
        }
        Award updatedAward = Award.of(awardRequestDto);
        updatedAward.setId(award.getId());
        awardRepository.save(updatedAward);
    }

    public void deleteAward(Long awardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Award award = getAwardByIdOrThrow(awardId);
        if (!award.getParticipants().contains(member.getName())) {
            throw new PermissionDeniedException("해당 수상 이력을 삭제할 권한이 없습니다.");
        }
        awardRepository.delete(award);
    }

    private Award getAwardByIdOrThrow(Long awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("해당 수상 이력이 존재하지 않습니다."));
    }

}
