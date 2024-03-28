package transit.transitwatch.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class BusStopCrowdingServiceImplTest {

    @Autowired
    private JPAQueryFactory query;

//    @DisplayName("버스 정류장 혼잡도 api 조회")
//    @Test
//    void api조회_테스트(){
//
//    }
//
//    @DisplayName("api 조회 후 db에 저장하기")
//    @Test
//    void 저장_테스트(){
//
//    }

//    @DisplayName("버스 정류장 혼잡도 조회하기")
//    @Test
//    void 조회_테스트(){
//        QBusStopCrowding qBusStopCrowding = QBusStopCrowding.busStopCrowding;
//        BusStopCrowding result = query
//                .selectFrom(qBusStopCrowding)
//                .fetchOne();
//
////        Assertions.assertThat(result).isEqualTo(busStopCrowding);
////        Assertions.assertThat(result.isEqualTo(busStopCrowding);
//        System.out.println("result = " + result);
//
//    }
}