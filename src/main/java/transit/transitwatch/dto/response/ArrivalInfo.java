package transit.transitwatch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class ArrivalInfo {
    @JsonProperty(value = "busId")
    private String routeId;
    @JsonProperty(value = "busType")
    private String routeType;                  // (1:공항, 2:마을, 3:간선, 4:지선, 5:순환, 6:광역, 7:인천, 8:경기, 9:폐지, 0:공용)
    private int firstArrivalBusTime;           // 첫번째도착예정버스의 시간
    private int secondArrivalBusTime;          // 두번째도착예정버스의 시간
    private String firstArrivalBusCrowding;
    private String secondArrivalBusCrowding;
    private String firstArrivalTimeBefore;     // 첫번째 도착예정 버스의 도착 - 몇 정거장 전
    private String secondArrivalTimeBefore;    // 두번째 도착예정 버스의 도착 - 몇 정거장 전

    @Builder
    public ArrivalInfo(String routeId, String routeType, int firstArrivalBusTime, int secondArrivalBusTime, String firstArrivalBusCrowding, String secondArrivalBusCrowding, String firstArrivalTimeBefore, String secondArrivalTimeBefore) {
        this.routeId = routeId;
        this.routeType = routeType;
        this.firstArrivalBusTime = firstArrivalBusTime;
        this.secondArrivalBusTime = secondArrivalBusTime;
        this.firstArrivalBusCrowding = firstArrivalBusCrowding;
        this.secondArrivalBusCrowding = secondArrivalBusCrowding;
        this.firstArrivalTimeBefore = firstArrivalTimeBefore;
        this.secondArrivalTimeBefore = secondArrivalTimeBefore;
    }
}
