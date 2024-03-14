package transit.transitwatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "COMMON_CODE")
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARENT_CODE", length = 5)
    private String parentCode;

    @Column(name = "CODE", length = 5)
    private String code;

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "USE_YN", length = 1)
    private String useYn;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;
}
