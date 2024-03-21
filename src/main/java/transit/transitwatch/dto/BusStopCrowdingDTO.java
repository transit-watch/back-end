package transit.transitwatch.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.util.DateTimestampAdapter;
import transit.transitwatch.util.DoubleTimestampAdapter;

import java.sql.Timestamp;

@ToString
@Getter
public class BusStopCrowdingDTO {

    private Long id;

    @SerializedName("itisCd")
    private String itisCd;

    @SerializedName("trsmUtcTime")
    @JsonAdapter(DoubleTimestampAdapter.class)
    private Timestamp sendUtcTime;

    @SerializedName("detcLot")
    private double yLongitude;

    @SerializedName("detcLat")
    private double xLatitude;

    @SerializedName("linkId")
    private String linkId;

    @SerializedName("sttnId")
    private String arsId;

    @SerializedName("trsmYear")
    private String sendPacketYear;

    @SerializedName("trsmMt")
    private String sendPacketMonth;

    @SerializedName("trsmDy")
    private String sendPacketDay;

    @SerializedName("trsmTm")
    private String sendPacketTime;

    @SerializedName("trsmMs")
    private String sendPacketMilisecond;

    @JsonAdapter(DateTimestampAdapter.class)
    @SerializedName("regDt")
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
