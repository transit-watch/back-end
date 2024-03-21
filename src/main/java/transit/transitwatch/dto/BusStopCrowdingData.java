package transit.transitwatch.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BusStopCrowdingData {

    @SerializedName("trsmUtcTime")
    private double trsmUtcTime;

    @SerializedName("trsmDy")
    private int trsmDy;

    @SerializedName("itisCd")
    private String itisCd;

    @SerializedName("trsmYear")
    private String trsmYear;

    @SerializedName("trsmMt")
    private String trsmMt;

    @SerializedName("trsmTm")
    private String trsmTm;

    @SerializedName("trsmMs")
    private String trsmMs;

    @SerializedName("detcLot")
    private double detcLot;

    @SerializedName("detcLat")
    private double detcLat;

    @SerializedName("linkId")
    private String linkId;

    @SerializedName("sttnId")
    private String sttnId;

    @SerializedName("regDt")
    private Date regDt;
}
