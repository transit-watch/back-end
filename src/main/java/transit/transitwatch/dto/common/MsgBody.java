package transit.transitwatch.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
/*
* 메시지 바디 - 본문 내용
* */

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class MsgBody <T> {
    @JsonProperty("itemList")
    private List<T> itemList;
}

