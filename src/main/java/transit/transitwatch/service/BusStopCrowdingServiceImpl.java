package transit.transitwatch.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.entity.QBusStopCrowding;
import transit.transitwatch.repository.BusStopCrowdingRepository;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.GsonUtil;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class BusStopCrowdingServiceImpl implements BusStopCrowdingService {

    private final BusStopCrowdingRepository busStopCrowdingRepository;
    private final JPAQueryFactory queryFactory;

    private final GsonUtil gsonUtil;
    private final ApiUtil apiUtil;
    @Value("${app.api.key}")
    private String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(BusStopCrowdingServiceImpl.class);

    /*
     * 버스 정류장 혼잡도 list 저장하기
     * */
    @Override
    public void saveBusStopCrowdingApi() throws Exception {
        String pageNo = "1";
        String numOfRows = "30";
        String url = "https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xBusStationCrowdedInformation/1.0" +
                "?pageNo=" + pageNo +
                "&numOfRows=" + numOfRows +
                "&apikey=" + apiKey;

        String apiResult;

        // api 받아오기
        try {
            apiResult = apiUtil.getApi(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // api 결과 dto에 넣기
        List<BusStopCrowdingDTO> busStopCrowdingDTOList = gsonUtil.busStopCrowdingParser(apiResult);

        // db에 저장하기
        try {
//            logger.debug("디버깅 시작");
            List<BusStopCrowding> busStopCrowdingList = busStopCrowdingDTOList.stream()
//                    .peek(dto -> logger.debug("Mapping DTO: {}", dto))
                    .map(BusStopCrowdingDTO::toEntity)
//                    .peek(entity -> logger.debug("Mapped entity: {}", entity))
                    .collect(Collectors.toList());
//            logger.debug("디버깅 끝");
            busStopCrowdingRepository.saveAll(busStopCrowdingList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 버스 정류장 혼잡도 조회
     * */
    @Override
    public List<BusStopCrowding> selectBusStopCrowding(BusStopCrowdingDTO busStopCrowdingDTO) throws Exception {

        QBusStopCrowding qBusStopCrowding = QBusStopCrowding.busStopCrowding;
        List<BusStopCrowding> resultList = queryFactory
                .selectFrom(qBusStopCrowding)
                .fetch();
        return resultList;
    }


}
