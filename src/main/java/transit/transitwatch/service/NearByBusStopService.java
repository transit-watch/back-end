package transit.transitwatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.BusStopLocationDTO;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.near.ItemNear;
import transit.transitwatch.dto.response.NearByBusStopResponse;
import transit.transitwatch.exception.ServiceException;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static transit.transitwatch.util.ErrorCode.GET_URL_FAIL;
import static transit.transitwatch.util.ErrorCode.SEARCH_FAIL;

/**
 * 좌표를 기반으로 주변 버스 정류장의 정보를 조회하고, 해당 데이터를 처리하는 서비스 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NearByBusStopService {

    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;
    @Value("${app.api.key.sbus}")
    private String serviceKey;
    private final BusStopCrowdingService busStopCrowdingService;
    private final BusStopLocationService busStopLocationService;

    /**
     * 지정된 좌표와 반경 내에서 버스 정류장 목록을 조회한다.
     *
     * @param tmX 경도 좌표
     * @param tmY 위도 좌표
     * @param radius 검색 반경(미터 단위)
     * @return 조회된 버스 정류장 목록을 포함하는 CommonApiDTO 객체를 반환한다.
     */
    @Cacheable(cacheNames = "near", key = "#tmX.toString() + '_' + #tmY.toString() + '_' + #radius")
    public CommonApiDTO<ItemNear> getNearByBusStopApi(double tmX, double tmY, int radius) {
        URI url = getApiUrl(tmX, tmY, radius);

        String apiResult = apiUtil.getApiUri(url);
        return apiJsonParser.busGoKrParser(apiResult, new TypeReference<>() {});
    }

    /**
     * 좌표기반 버스 정류장 검색 API URL을 생성한다.
     *
     * @param tmX 경도 좌표
     * @param tmY 위도 좌표
     * @param radius 검색 반경
     * @return 생성된 URI
     */
    private URI getApiUrl(double tmX, double tmY, int radius) {
        String url = String.format("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?tmX=%f&tmY=%f&radius=%d&resultType=json&serviceKey=%s",
                tmX, tmY, radius, serviceKey);
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            log.error("Url 가져오기에 실패했습니다. Url = {}", url, e);
            throw new ServiceException(GET_URL_FAIL);
        }
    }

    /**
     * API로부터 받은 데이터를 기반으로 버스 정류장 목록의 응답 객체를 생성한다.
     *
     * @param commonApiDTO API 응답 데이터를 포함하는 DTO
     * @return 버스 정류장 목록 응답 객체의 리스트
     */
    public List<NearByBusStopResponse> getNearByBusStopResponses(CommonApiDTO<ItemNear> commonApiDTO) {
        return commonApiDTO.getMsgBody().getItemList().stream()
                .map(this::createResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 개별 버스 정류장 데이터를 기반으로 응답 객체를 생성한다.
     *
     * @param item 버스 정류장 데이터 항목
     * @return 생성된 NearByBusStopResponse 객체
     */
    private NearByBusStopResponse createResponse(ItemNear item) {
        try {
            if(item.getArsId().equals("0")) return null;

            ItisCdEnum itisCdEnum = busStopCrowdingService.selectBusStopCrowding(item.getArsId());
            BusStopLocationDTO location = busStopLocationService.selectBusStopLocation(item.getArsId()).orElseGet(BusStopLocationDTO::new);

            return NearByBusStopResponse.builder()
                    .stationId(item.getStationId())
                    .stationName(location.getStationName())
                    .arsId(item.getArsId())
                    .yLatitude(location.getYLatitude())
                    .xLongitude(location.getXLongitude())
                    .distance(Integer.parseInt(item.getDist()))
                    .crowding(itisCdEnum.name())
                    .build();

        } catch (NumberFormatException e) {
            log.error("좌표기반 근처 버스 정류장 정보 좌표 변환 중 오류가 발생했습니다. : {}", item, e);
            throw new ServiceException(SEARCH_FAIL);
        } catch (EntityNotFoundException e) {
            log.error("좌표기반 근처 버스 정류장 정보가 없습니다. : {}", item.getArsId(), e);
            throw new ServiceException(SEARCH_FAIL);
        } catch (Exception e) {
            log.error("좌표기반 근처 버스 정류장 목록 조회에 실패했습니다. : {}", item.getArsId(), e);
            throw new ServiceException(SEARCH_FAIL);
        }
    }
}
