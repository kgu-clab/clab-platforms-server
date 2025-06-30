package page.clab.api.domain.members.support.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "support_answer")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE support_answer SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class SupportAnswerJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "support_id",nullable = false)
    private Long supportId;

    @Column(name = "admin_id", nullable = false)
    private String adminId;

    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.support.content}")
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

}
