package transit.transitwatch.dto.near;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
* 좌표 기반 근처 정류장 목록 api 상세
* */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class ItemNear {
    @JsonProperty("stationId")
    private String stationId;

    @JsonProperty("arsId")
    private String arsId;

    @JsonProperty("dist")
    private String dist;
}