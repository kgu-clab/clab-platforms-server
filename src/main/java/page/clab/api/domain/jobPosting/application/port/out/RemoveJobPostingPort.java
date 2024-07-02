package page.clab.api.domain.jobPosting.application.port.out;

public interface RemoveJobPostingPort {
    void deleteById(Long id);
}