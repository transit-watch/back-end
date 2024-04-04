package transit.transitwatch.dto.arrival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 정류장 별 / 버스 노선 별 도착 정보 api 상세
 * */

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class ItemArrival {
    @JsonProperty("stId")
    private String stationId;          // 정류소 고유 ID

    @JsonProperty("stNm")
    private String stationName;          // 정류소명

    @JsonProperty("arsId")
    private String arsId;         // 정류소 번호

    @JsonProperty("busRouteId")
    private String busRouteId;    // 노선ID

    @JsonProperty("routeType")
    private String routeType;        // 노선유형 (1:공항, 2:마을, 3:간선, 4:지선, 5:순환, 6:광역, 7:인천, 8:경기, 9:폐지, 0:공용)

    @JsonProperty("mkTm")
    private String makeTime;          // 제공시각

    @JsonProperty("traTime1")
    private int firstBusTime;         // 첫번째도착예정버스의 여행시간 (분)

    @JsonProperty("rerdie_Div1")
    private int firstBusDiv;      // 첫번째 버스의 버스내부 제공용 현재 재차 구분 : reride_Num1값의 의미 구분 (0: 데이터 없음, 2: 재차인원, 4:혼잡도)

    @JsonProperty("reride_Num1")
    private String firstBusCrowding;      // 첫번째 버스의 버스내부 제공용 현재 재차 인원 : 재차구분(rerdie_Div1값이) 4일 때 혼잡도(0: 데이터없음, 3: 여유, 4: 보통, 5: 혼잡)

    @JsonProperty("traTime2")
    private int secondBusTime;         // 두번째도착예정버스의 여행시간 (분)

    @JsonProperty("rerdie_Div2")
    private int secondBusDiv;      // 두번째 버스의 버스내부 제공용 현재 재차 구분 : reride_Num2값의 의미 구분 (0: 데이터 없음, 2: 재차인원, 4:혼잡도)

    @JsonProperty("reride_Num2")
    private String secondBusCrowding;      // 두번째 버스의 버스내부 제공용 현재 재차 인원 : 재차구분(rerdie_Div2값이) 4일 때 혼잡도(0: 데이터없음, 3: 여유, 4: 보통, 5: 혼잡)

    @JsonProperty("arrmsg1")
    private String firstArrivalMessage;       // 첫번째 도착예정 버스의 도착정보메시지

    @JsonProperty("arrmsg2")
    private String secondArrivalMessage;       // 두번째 도착예정 버스의 도착정보메시지

}