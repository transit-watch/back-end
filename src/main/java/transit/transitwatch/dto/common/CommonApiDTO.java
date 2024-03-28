package transit.transitwatch.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
* 서울특별시 전용 - 공공 데이터 api 공통
* */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class CommonApiDTO {

    private ComMsgHeader comMsgHeader; // 공통메시지 헤더
    private MsgHeader msgHeader; // 메시지 헤더
    private MsgBody msgBody; // 본문 내용
}


