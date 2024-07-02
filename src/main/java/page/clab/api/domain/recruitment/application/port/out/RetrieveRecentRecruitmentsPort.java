package page.clab.api.domain.recruitment.application.port.out;

import page.clab.api.domain.recruitment.domain.Recruitment;

import java.util.List;

public interface RetrieveRecentRecruitmentsPort {
    List<Recruitment> findTop5ByOrderByCreatedAtDesc();
}
