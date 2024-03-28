package transit.transitwatch.entity.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
* 정류장 혼잡정보 뷰 전용 -
* */

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BusStopCrowdingView {
    @Id
    private Long id;
    private String itisCd;
    private String linkId;
    private String arsId;
}
