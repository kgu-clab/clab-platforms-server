package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @Size(min = 9, max = 9)
    private String studentId;

    @Column(nullable = false)
    @Size(max = 10)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long grade;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDate birth;

    @Column(nullable = false)
    private String address;

    private String interests;

    private String otherActivities;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}