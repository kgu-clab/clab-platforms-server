package page.clab.api.domain.jobPosting.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.jobPosting.dao.JobPostingRepository;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    private final EntityManager entityManager;
    
    public Long createJobPosting(JobPostingRequestDto jobPostingRequestDto) {
        JobPosting jobPosting = JobPosting.of(jobPostingRequestDto);
        JobPosting existingJobPosting = getJobPostingByJobPostingUrl(jobPostingRequestDto);
        if (existingJobPosting != null) {
            jobPosting.setId(existingJobPosting.getId());
            jobPosting.setCreatedAt(existingJobPosting.getCreatedAt());
        }
        return jobPostingRepository.save(jobPosting).getId();
    }

    public PagedResponseDto<JobPostingResponseDto> getJobPostings(Pageable pageable) {
        Page<JobPosting> jobPostings = jobPostingRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(jobPostings.map(JobPostingResponseDto::of));
    }

    public JobPostingDetailsResponseDto getJobPosting(Long jobPostingId) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        return JobPostingDetailsResponseDto.of(jobPosting);
    }

    public PagedResponseDto<JobPostingResponseDto> searchJobPostings(String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobPosting> criteriaQuery = criteriaBuilder.createQuery(JobPosting.class);
        Root<JobPosting> root = criteriaQuery.from(JobPosting.class);
        List<Predicate> predicates = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            String keywordLowerCase = "%" + keyword.toLowerCase() + "%";
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), keywordLowerCase)
            ));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));
        TypedQuery<JobPosting> query = entityManager.createQuery(criteriaQuery);

        List<JobPosting> jobPostings = query.getResultList();
        Set<JobPosting> uniqueJobPostings = new LinkedHashSet<>(jobPostings);
        List<JobPosting> distinctJobPostings = new ArrayList<>(uniqueJobPostings);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), distinctJobPostings.size());
        Page<JobPosting> jobPostingPage = new PageImpl<>(distinctJobPostings.subList(start, end), pageable, distinctJobPostings.size());

        return new PagedResponseDto<>(jobPostingPage.map(JobPostingResponseDto::of));
    }

    public Long updateJobPosting(Long jobPostingId, JobPostingUpdateRequestDto jobPostingUpdateRequestDto) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        jobPosting.update(jobPostingUpdateRequestDto);
        return jobPostingRepository.save(jobPosting).getId();
    }

    public Long deleteJobPosting(Long jobPostingId) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        jobPostingRepository.delete(jobPosting);
        return jobPostingId;
    }

    public JobPosting getJobPostingByIdOrThrow(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 채용 공고입니다."));
    }

    public JobPosting getJobPostingByJobPostingUrl(JobPostingRequestDto jobPostingRequestDto) {
        return jobPostingRepository.findByJobPostingUrl(jobPostingRequestDto.getJobPostingUrl()).orElse(null);
    }

}
