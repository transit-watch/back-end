package transit.transitwatch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import transit.transitwatch.dto.common.CommonApiDTO;

class ApiJsonParserTest {

    @Test
    void busStopCrowdingParser() {
    }

    @DisplayName("공공데이터 api parsing 테스트")
    @Test
    void nearByBusStopParser() throws Exception{
        String jsonInput = "{\"comMsgHeader\":{\"errMsg\":null,\"responseTime\":null,\"responseMsgID\":null,\"requestMsgID\":null,\"returnCode\":null,\"successYN\":null},\"msgHeader\":{\"headerMsg\":\"정상적으로 처리되었습니다.\",\"headerCd\":\"0\",\"itemCount\":0},\"msgBody\":{\"itemList\":[{\"stationId\":\"102900092\",\"stationNm\":\"도원삼성래미안아파트단지내\",\"gpsX\":\"126.9553881353\",\"gpsY\":\"37.5381983039\",\"posX\":\"196057.69260830848\",\"posY\":\"448750.08064129343\",\"stationTp\":\"0\",\"arsId\":\"03737\",\"dist\":\"48\"},{\"stationId\":\"102900096\",\"stationNm\":\"도원삼성래미안아파트101동앞\",\"gpsX\":\"126.9562965631\",\"gpsY\":\"37.5390222559\",\"posX\":\"196138.01199687444\",\"posY\":\"448841.4817549079\",\"stationTp\":\"0\",\"arsId\":\"03520\",\"dist\":\"75\"},{\"stationId\":\"102900097\",\"stationNm\":\"도원삼성래미안아파트101동앞\",\"gpsX\":\"126.9562320809\",\"gpsY\":\"37.5390685906\",\"posX\":\"196132.31622293708\",\"posY\":\"448846.62644026056\",\"stationTp\":\"0\",\"arsId\":\"03511\",\"dist\":\"77\"},{\"stationId\":\"113900079\",\"stationNm\":\"하나님의교회\",\"gpsX\":\"126.9553127738\",\"gpsY\":\"37.539331855\",\"posX\":\"196051.09276686702\",\"posY\":\"448875.8806538596\",\"stationTp\":\"0\",\"arsId\":\"14600\",\"dist\":\"109\"}]}}";

        ObjectMapper objectMapper = new ObjectMapper();

        CommonApiDTO result = objectMapper.readValue(jsonInput, CommonApiDTO.class);
        System.out.println(result);

        Assertions.assertThat(result.getMsgHeader().getHeaderMsg()).isEqualTo("정상적으로 처리되었습니다.");

    }
}