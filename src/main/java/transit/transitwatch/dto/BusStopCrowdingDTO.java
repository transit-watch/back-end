package transit.transitwatch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.util.DateTimestampDeserializer;
import transit.transitwatch.util.DoubleTimestampDeserializer;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class BusStopCrowdingDTO {

    private Long id;

    @JsonProperty("itisCd")
    private String itisCd;

    @JsonProperty("trsmUtcTime")
    @JsonDeserialize(using = DoubleTimestampDeserializer.class)
    private Timestamp sendUtcTime;

    @JsonProperty("detcLot")
    private double yLongitude;

    @JsonProperty("detcLat")
    private double xLatitude;

    @JsonProperty("linkId")
    private String linkId;

    @JsonProperty("sttnId")
    private String arsId;

    @JsonProperty("trsmYear")
    private String sendPacketYear;

    @JsonProperty("trsmMt")
    private String sendPacketMonth;

    @JsonProperty("trsmDy")
    private String sendPacketDay;

    @JsonProperty("trsmTm")
    private String sendPacketTime;

    @JsonProperty("trsmMs")
    private String sendPacketMilisecond;

    @JsonDeserialize(using = DateTimestampDeserializer.class)
    @JsonProperty("regDt")
    private Timestamp recordDate;

    private Timestamp registerDate;
    private Timestamp editDate;
    private String url;

    public BusStopCrowdingDTO(Long id, String itisCd, Timestamp sendUtcTime, double yLongitude, double xLatitude, String linkId, String arsId, String sendPacketYear, String sendPacketMonth, String sendPacketDay, String sendPacketTime, String sendPacketMilisecond, Timestamp recordDate, Timestamp registerDate, Timestamp editDate, String url) {
        this.id = id;
        this.itisCd = itisCd;
        this.sendUtcTime = sendUtcTime;
        this.yLongitude = yLongitude;
        this.xLatitude = xLatitude;
        this.linkId = linkId;
        this.arsId = arsId;
        this.sendPacketYear = sendPacketYear;
        this.sendPacketMonth = sendPacketMonth;
        this.sendPacketDay = sendPacketDay;
        this.sendPacketTime = sendPacketTime;
        this.sendPacketMilisecond = sendPacketMilisecond;
        this.recordDate = recordDate;
        this.registerDate = registerDate;
        this.editDate = editDate;
        this.url = url;
    }

    /*
     * Entity로 변환
     * */
    public BusStopCrowding toEntity() {
        return new BusStopCrowding(
                this.id,
                this.itisCd,
                this.sendUtcTime,
                this.yLongitude,
                this.xLatitude,
                this.linkId,
                this.arsId,
                this.sendPacketYear,
                this.sendPacketMonth,
                this.sendPacketDay,
                this.sendPacketTime,
                this.sendPacketMilisecond,
                this.recordDate,
                this.registerDate,
                this.editDate
        );
    }
}
