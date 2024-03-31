package transit.transitwatch.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
* 메시지 헤더 - 메시지만 나오고 나머진 항상 0
* */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class MsgHeader {
    @JsonProperty("headerMsg")
    private String headerMsg;
    @JsonProperty("headerCd")
    private String headerCd;
    @JsonProperty("itemCount")
    private int itemCount;
}
