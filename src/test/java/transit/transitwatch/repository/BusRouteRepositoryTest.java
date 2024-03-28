package transit.transitwatch.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.entity.QBusRoute;
import transit.transitwatch.entity.QBusStopInfo;

import java.util.List;

@SpringBootTest
class BusRouteRepositoryTest {
    @Autowired
    private JPAQueryFactory query;

    @DisplayName("정류장에 있는 버스노선 검색-어떤 방향인지 까지 포함")
    @Test
    void 조회(){

        QBusRoute busRouteA = new QBusRoute("a");
        QBusRoute busRouteB = new QBusRoute("b");
        QBusStopInfo busStopInfoC = new QBusStopInfo("c");

       List<RouteInfo> routeInfoList = query
               .select(Projections.fields(RouteInfo.class,
                        busRouteA.routeId,
                        busRouteA.routeName,
                        busStopInfoC.stationName.as("direction"),
                        busRouteA.stationId,
                        busRouteA.arsId
                       )
                )
                .from(busRouteA)
                .join(busRouteB)
                .on(busRouteA.routeOrder.add(1).eq(busRouteB.routeOrder)
                        .and(busRouteA.routeId.eq(busRouteB.routeId)))
                .join(busStopInfoC)
                .on(busRouteB.stationId.eq(busStopInfoC.stationId))
                .where(busRouteA.arsId.eq("02137"))
                .fetch();

        for (RouteInfo routeInfo : routeInfoList) {
            System.out.println("routeInfo.getDirection() = " + routeInfo.getDirection());
        }
    }

}