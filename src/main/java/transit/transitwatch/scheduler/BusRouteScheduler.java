package transit.transitwatch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import transit.transitwatch.service.BusRouteService;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusRouteScheduler {
    private final BusRouteService busRouteService;

    // 매달 둘째 주 월요일 새벽 2시에 실행
    @Async
    @Scheduled(cron = "0 0 2 ? * 2#2") // 초 분 시간 일 월 요일
    public void run() {
        try {
//            busRouteService.saveBusRouteFile("bus_route.csv");
            log.info("버스 노선 정보 갱신 Scheduler 실행");
        } catch (Exception e) {
            log.error("버스 노선 정보 갱신 Scheduler 실행 중 에러 발생", e);
        }
    }
}
