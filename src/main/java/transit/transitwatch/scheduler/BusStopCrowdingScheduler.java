package transit.transitwatch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import transit.transitwatch.service.BusStopCrowdingService;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusStopCrowdingScheduler {

    private final BusStopCrowdingService busStopCrowdingService;

    @Scheduled(cron = "0 * 5-23 * * *") // 매일 새벽 5시부터 밤 11시까지 매 분마다 실행
    public void run() {
        int pageNo = 10100;
        int numOfRows = 10000;
        boolean hasData = true;

//        while (hasData) {
//            try {
//                hasData = busStopCrowdingService.saveBusStopCrowdingApi(pageNo, numOfRows);
//                log.info("버스 정류장 혼잡도 Scheduler 실행: 페이지 번호 = {}", pageNo);
//                pageNo++;
//            } catch (Exception e) {
//                log.error("버스 정류장 혼잡도 데이터 처리 중 오류 발생: 페이지 번호 = {}", pageNo, e);
//                break;
//            }
//        }
    }
}
