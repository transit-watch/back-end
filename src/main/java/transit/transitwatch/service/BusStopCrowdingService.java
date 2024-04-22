package transit.transitwatch.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.exception.ServiceException;
import transit.transitwatch.repository.BusStopCrowdingRepository;
import transit.transitwatch.repository.projections.BusStopCrowdingProjection;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static transit.transitwatch.util.ErrorCode.FILE_SAVE_FAIL;
import static transit.transitwatch.util.ErrorCode.GET_URL_FAIL;

/**
 * API를 통해 버스 정류장의 혼잡도 정보를 수집하고, DB에 저장하는 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BusStopCrowdingService {

    private final BusStopCrowdingRepository busStopCrowdingRepository;
    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;

    @Value("${app.api.key.tdata}")
    private String apiKey;

    /**
     * 버스 정류장의 혼잡도 데이터를 API로부터 가져와 DB에 저장한다.
     * API로부터 유효한 데이터를 받지 못하면 저장하지 않고 false를 반환한다.
     *
     * @param pageNo 요청할 페이지 번호
     * @param numOfRows 페이지 당 행 수
     * @return 데이터 저장 성공 여부 (true: 성공, false: 실패)
     * @throws ServiceException 데이터 수집 또는 저장 중 발생하는 예외
     */
    public boolean saveBusStopCrowdingApi(int pageNo, int numOfRows) {
        // Url 가져오기
        URI uri = getApiUrl(pageNo, numOfRows);
        String apiResult;

        try {
            apiResult = apiUtil.getApiUri(uri);
            if (apiResult == null || apiResult.isEmpty() || apiResult.equals("[]")) {
                log.info("API에서 데이터가 반환되지 않았습니다. (페이지 번호: {}, 행 수: {})", pageNo, numOfRows);
                return false;
            }

            List<BusStopCrowdingDTO> busStopCrowdingDTOList = apiJsonParser.busStopCrowdingParser(apiResult);
            busStopCrowdingDTOList.forEach(this::upsertBusStopCrowdingDTO);
            return true;
        } catch (Exception e) {
            log.error("버스 정류장 혼잡도 데이터 수집 중 오류가 발생했습니다.", e);
            throw new ServiceException(FILE_SAVE_FAIL);
        }
    }

    /**
     * API로부터 받은 혼잡도 데이터를 DB에 저장하거나 업데이트한다.
     *
     * @param dto 저장할 혼잡도 데이터
     * @throws ServiceException 데이터 저장 실패 시 발생
     */
    private void upsertBusStopCrowdingDTO(BusStopCrowdingDTO dto) {
        try {
            busStopCrowdingRepository.upsertBusStopCrowding(
                    dto.getItisCd(),
                    dto.getSendUtcTime(),
                    dto.getXLongitude(),
                    dto.getYLatitude(),
                    dto.getLinkId(),
                    dto.getArsId(),
                    dto.getSendPacketYear(),
                    dto.getSendPacketMonth(),
                    dto.getSendPacketDay(),
                    dto.getSendPacketTime(),
                    dto.getSendPacketMilisecond(),
                    dto.getRecordDate());
        } catch (DataAccessException e) {
            log.error("버스 정류장 데이터 저장에 실패했습니다.: 정류장 ARS ID {}", dto.getArsId(), e);
            throw new ServiceException(FILE_SAVE_FAIL);
        }
    }

    /**
     * 혼잡도 정보 API의 URL을 생성한다.
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지당 행 수
     * @return 생성된 URI
     * @throws ServiceException URL 생성 실패 시 발생
     */
    private URI getApiUrl(int pageNo, int numOfRows) {
        String url = String.format("https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xBusStationCrowdedInformation/1.0?pageNo=%d&numOfRows=%d&apikey=%s",
                pageNo, numOfRows, apiKey);
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            log.error("Url 가져오기에 실패했습니다. Url = {}", url, e);
            throw new ServiceException(GET_URL_FAIL);
        }
    }

    /**
     * 특정 ARS ID를 사용하여 버스 정류장의 혼잡도 정보를 조회한다.
     * 조회된 혼잡도 정보가 없을 경우 기본적으로 '여유' 상태를 반환한다.
     *
     * @param arsId 조회할 ARS ID
     * @return 혼잡도 상태 (enum)
     */
    public ItisCdEnum selectBusStopCrowding(String arsId) {

        return busStopCrowdingRepository.findByArsId(arsId)
                .map(BusStopCrowdingProjection::getItisCd)
                .map(apiUtil::getCrowdingEnum)
                .orElse(ItisCdEnum.EASYGOING);
    }
}
