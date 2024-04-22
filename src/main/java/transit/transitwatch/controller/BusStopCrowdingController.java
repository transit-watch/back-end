package transit.transitwatch.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.service.BusStopCrowdingService;

/**
 * 버스 정류장 혼잡도 데이터 관리 컨트롤러.
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
public class BusStopCrowdingController {

    private final BusStopCrowdingService busStopCrowdingService;

    /**
     * 버스 정류장 혼잡도 정보를 외부 API로부터 조회하여 DB에 저장하는 메서드.
     * <p>이 메서드는 외부 혼잡도 API를 호출하고 결과 데이터를 DB에 저장한다. API 호출에 필요한 페이지 번호와 행의 수는 매개변수로 전달된다.</p>
     *
     * @param pageNo    조회할 페이지 번호
     * @param numOfRows 페이지 당 데이터 수
     * @return 작업 성공 여부가 담긴 Response 객체.
     * @throws RuntimeException 혼잡도 정보 조회 또는 DB 저장 중 예외가 발생한 경우
     */
    @PostMapping("/api/v1/bus-stops/crowding")
    public Response<String> saveBusStopCrowding(@RequestParam("pageNo") @Positive @Min(0) @Max(50) int pageNo,
                                                @RequestParam("numOfRows") @Positive @Min(0) @Max(1000) int numOfRows) {
        busStopCrowdingService.saveBusStopCrowdingApi(pageNo, numOfRows);
        return Response.ok("버스 정류장 혼잡도 API 조회 -> DB에 저장 성공");
    }
}
