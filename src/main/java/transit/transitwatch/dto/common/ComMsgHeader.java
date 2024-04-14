package transit.transitwatch.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
* 공통메시지 헤더 - 항상 결과가 null(?)
* */

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class ComMsgHeader {
    @JsonProperty("errMsg")
    private String errMsg;
    @JsonProperty("responseTime")
    private String responseTime;
    @JsonProperty("responseMsgID")
    private String responseMsgID;
    @JsonProperty("requestMsgID")
    private String requestMsgID;
    @JsonProperty("returnCode")
    private String returnCode;
    @JsonProperty("successYN")
    private String successYN;
}