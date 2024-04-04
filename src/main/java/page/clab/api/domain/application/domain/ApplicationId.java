package page.clab.api.domain.application.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class ApplicationId implements Serializable {

    @EqualsAndHashCode.Include
    private String studentId;

    @EqualsAndHashCode.Include
    private Long recruitmentId;

    public static ApplicationId create(String studentId, Long recruitmentId) {
        return new ApplicationId(studentId, recruitmentId);
    }

}
