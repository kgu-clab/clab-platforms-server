package page.clab.api.domain.memberManagement.executive.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.memberManagement.executive.domain.ExecutivePosition;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "executive")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE executive SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ExecutiveJpaEntity extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @Size(min = 9, max = 9, message = "{size.executive.id}")
    private String id;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.executive.name}")
    private String name;

    @Column(nullable = false)
    @Email(message = "{email.executive.email}")
    @Size(min = 1, message = "{size.executive.email}")
    private String email;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutivePosition position;

    private String imageUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
