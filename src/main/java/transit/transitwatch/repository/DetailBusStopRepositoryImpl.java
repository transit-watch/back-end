package transit.transitwatch.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.entity.QBusRoute;

import java.util.List;

import static transit.transitwatch.entity.QBusRoute.busRoute;
import static transit.transitwatch.entity.QBusStopInfo.busStopInfo;

@RequiredArgsConstructor
@Repository
public class DetailBusStopRepositoryImpl implements DetailBusStopRepositoryCustom {
    private final JPAQueryFactory query;

    /*
     * 정류장 상세정보 조회 - 모든 노선 조회
     * */
    @Override
    public List<RouteInfo> searchDetailBusStopList(String arsId) {
        QBusRoute busRouteB = new QBusRoute("b");

        List<RouteInfo> routeInfoList = query
                .select(Projections.fields(RouteInfo.class,
                                busRoute.routeId,
                                busRoute.routeName,
                                busRoute.routeOrder,
                                busStopInfo.stationName.as("direction"),
                                busRoute.stationId,
                                busRoute.arsId
                        )
                )
                .from(busRoute)
                .join(busRouteB)
                .on(busRoute.routeOrder.add(1).eq(busRouteB.routeOrder) // 다음순서 + 1 = 다음역
                        .and(busRoute.routeId.eq(busRouteB.routeId)))
                .join(busStopInfo)
                .on(busRouteB.stationId.eq(busStopInfo.stationId))
                .where(arsEq(arsId))
                .fetch();
        return routeInfoList;
    }

    private BooleanExpression arsEq(String arsId) {
        return arsId.isEmpty() ? null : busRoute.arsId.eq(arsId);
    }

}
