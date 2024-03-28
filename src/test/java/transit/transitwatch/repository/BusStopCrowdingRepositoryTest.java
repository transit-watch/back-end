package transit.transitwatch.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.entity.QBusStopCrowding;

import java.util.List;

@Transactional
@SpringBootTest
class BusStopCrowdingRepositoryTest {
    @Autowired
    private JPAQueryFactory query;
    @DisplayName("버스 정류장 혼잡도 조회하기")
    @Test
    void 혼잡_조회_테스트(){
        QBusStopCrowding qBusStopCrowding = QBusStopCrowding.busStopCrowding;
        List<BusStopCrowding> result = query
                .selectFrom(qBusStopCrowding)
                .where(qBusStopCrowding.arsId.eq("12021"))
                .fetch();
//        BusStopCrowding busStopCrowding =
//        assertThat(result).isEqualTo(busStopCrowding);
        System.out.println("result = " + result);
    }
}