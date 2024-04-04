package transit.transitwatch.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.entity.QBusStopCrowding;
import transit.transitwatch.repository.projections.BusStopCrowdingProjection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BusStopCrowdingRepositoryTest {

    @Autowired
    BusStopCrowdingRepository busStopCrowdingRepository;

    @Autowired
    private JPAQueryFactory query;

    @DisplayName("버스 정류장 혼잡도 조회하기")
    @Test
    void 혼잡_조회_테스트() {
        QBusStopCrowding qBusStopCrowding = QBusStopCrowding.busStopCrowding;
        List<BusStopCrowding> result = query
                .selectFrom(qBusStopCrowding)
                .where(qBusStopCrowding.arsId.eq("12021"))
                .fetch();

        System.out.println("result = " + result);
    }

    @DisplayName("정류장 혼잡도 1건 조회")
    @Test
    void 혼잡_조회() {
        String arsId = "12021";

        Optional<BusStopCrowdingProjection> result = busStopCrowdingRepository.findByArsId(arsId);
        System.out.println("result = " + result.get().getArsId());
        assertThat(result.get().getArsId()).isIn(arsId);
    }

    @DisplayName("정류장 혼잡도 다건 조회")
    @Test
    void 혼잡_조회2() {
        List<String> arsIdList = new ArrayList<>();
        arsIdList.add("19006");
        arsIdList.add("12021");
        arsIdList.add("13044");

        List<BusStopCrowdingProjection> latestByArsId = busStopCrowdingRepository.findByArsIdIn(arsIdList);
        for (BusStopCrowdingProjection result : latestByArsId) {
            System.out.println("result = " + result.getLinkId());
            assertThat(result.getArsId()).isIn(arsIdList);
        }
    }
}