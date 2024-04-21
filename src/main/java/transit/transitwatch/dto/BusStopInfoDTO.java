package transit.transitwatch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import transit.transitwatch.entity.BusStopInfo;

@ToString
@Getter
public class BusStopInfoDTO {

    private String stationId;
    private String stationName;
    private String arsId;
    private String linkId;
    private double yLatitude;
    private double xLongitude;
    @JsonIgnore
    private char useYN; // 사용 여부 (1: 사용, 0: 미사용)
    @JsonIgnore
    private char virtualBusStopYN; // 가상 정류장 여부 (1: 가상정류장, 0: 일반정류장)

    @Builder
    public BusStopInfoDTO(String stationId, String stationName, String arsId, String linkId, double yLatitude, double xLongitude, char useYN, char virtualBusStopYN) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.linkId = linkId;
        this.yLatitude = yLatitude;
        this.xLongitude = xLongitude;
        this.useYN = useYN;
        this.virtualBusStopYN = virtualBusStopYN;
    }

    /*
     * Entity로 변환
     * */
    public BusStopInfo toEntity() {
        return new BusStopInfo(
                stationId,
                stationName,
                arsId,
                linkId,
                yLatitude,
                xLongitude,
                useYN,
                virtualBusStopYN
        );
    }
}
