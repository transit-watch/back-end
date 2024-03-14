package transit.transitwatch.dto;

import lombok.Data;

@Data
public class BusStopInfoDTO {

    private String stationId;
    private String stationName;
    private String arsId;
    private String linkId;
    private Double xLatitude;
    private Double yLongitude;
    private char useYN; // 사용 여부 (1: 사용, 0: 미사용)
    private char virtualBusStopYN; // 가상 정류장 여부 (1: 가상정류장, 0: 일반정류장)

    public BusStopInfoDTO(String stationId, String stationName, String arsId, String linkId, Double xLatitude, Double yLongitude,char useYN,char virtualBusStopYN) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.linkId = linkId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
        this.useYN = useYN;
        this.virtualBusStopYN = virtualBusStopYN;
    }
}
