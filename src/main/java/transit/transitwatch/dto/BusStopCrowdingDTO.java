package transit.transitwatch.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BusStopCrowdingDTO {
    private Long id;
    private String itisCd;
    private Timestamp sendUtcTime;
    private Double yLongitude;
    private Double xLatitude;
    private String linkId;
    private String arsId;
    private String sendPacketYear;
    private String sendPacketMonth;
    private String sendPacketDay;
    private String sendPacketTime;
    private String sendPacketMilisecond;
    private Timestamp recordDate;
    private Timestamp registerDate;
    private Timestamp editDate;
    private String url;
}
