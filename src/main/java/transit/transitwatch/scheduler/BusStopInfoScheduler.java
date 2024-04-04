package transit.transitwatch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import transit.transitwatch.service.BusStopInfoService;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusStopInfoScheduler {

    private final BusStopInfoService busStopInfoService;

    // 분기 별 1회 업데이트 (월:1,4,7,10 / 시: 새벽 3시 / 둘째주 월요일
    @Scheduled(cron = "0 0 3 ? 1,4,7,10 2#2") // 초 분 시간 일 월 요일
    public void run() {
        try {
            try {
                try {
//                    busStopInfoService.saveBusStopInfoFile("bus_stop_info.csv");
                    log.info("버스 정류장 정보 갱신 Scheduler 실행");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
