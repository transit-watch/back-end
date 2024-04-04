package transit.transitwatch.dto;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("AutocompleteSearch")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class SearchKeywordDTO {

    private long id;
    private String stationId;
    private String stationName;
    private String arsId;
    private double xLatitude;
    private double yLongitude;

    @Builder
    public SearchKeywordDTO(String stationId, String stationName, String arsId, double xLatitude, double yLongitude) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
    }
}
