package transit.transitwatch.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import transit.transitwatch.repository.projections.SearchKeywordProjection;

import java.util.List;

@SpringBootTest
class BusStopInfoRepositoryTest {

    @Autowired
    BusStopInfoRepository busStopInfoRepository;
    @Test
    void selectBusStopInfo() {

        List<SearchKeywordProjection> searchKeywordDTOS = busStopInfoRepository.selectBusStopInfo();
        System.out.println("searchKeywordDTOS = " + searchKeywordDTOS.get(0).getStationId());
    }
}