package page.clab.api.domain.jobPosting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmploymentType {

    FULL_TIME("FULL_TIME", "정규직"),
    CONTRACT("CONTRACT", "계약직"),
    INTERN("INTERN", "인턴"),
    ASSISTANT("ASSISTANT", "어시스턴트"),
    PART_TIME("PART_TIME", "파트타임");

    private final String key;
    private final String description;

}