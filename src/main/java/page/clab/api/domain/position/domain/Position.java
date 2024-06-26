package page.clab.api.domain.position.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE \"position\" SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    private PositionType positionType;

    @Column(nullable = false)
    private String year;

    public static Position create(String memberId) {
        return Position.builder()
                .memberId(memberId)
                .positionType(PositionType.MEMBER)
                .year(String.valueOf(LocalDate.now().getYear()))
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

}
