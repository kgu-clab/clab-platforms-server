package page.clab.api.type.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class AttendanceId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @EqualsAndHashCode.Include
    @Column(name = "QR_code_data")
    private String QRCodeData;

}
