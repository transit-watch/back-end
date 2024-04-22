package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.service.BusStopInfoService;

/**
 * 버스 정류장 정보 관리 컨트롤러.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class BusStopInfoController {

    private final BusStopInfoService busStopInfoService;

    /**
     * 버스 정류장 정보 CSV 파일을 다운로드하여 DB에 저장한다.
     * @return 작업 성공 여부를 나타내는 응답 메시지
     * @throws RuntimeException 파일 다운로드 또는 DB 저장 중 예외 발생 시
     */
    @PostMapping("/api/v1/bus-stops/info")
    public Response<String> downloadAndSaveBusStopInfo() {
        busStopInfoService.saveBusStopInfoFile("bus_stop_info.csv");

        return Response.ok("버스 정류장 정보 CSV 파일 다운로드 -> DB에 저장 성공");
    }
}
