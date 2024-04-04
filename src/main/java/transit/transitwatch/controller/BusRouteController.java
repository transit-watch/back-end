package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.service.BusRouteService;

/**
 * 버스 노선 데이터 관리 컨트롤러.
 */
@RequiredArgsConstructor
@RestController
public class BusRouteController {

    private final BusRouteService busRouteService;

    /**
     * 버스 노선(경로) 정보 파일을 다운로드하고, 엑셀 파일을 CSV 형식으로 변환한 후 DB에 저장한다.
     * <p>이 메서드는 다음 작업을 순차적으로 수행한다:
     * <ol>
     *     <li>버스 노선 경로 파일 다운로드</li>
     *     <li>XLSX 파일을 CSV 형식으로 변환</li>
     *     <li>변환된 CSV 파일을 DB에 저장</li>
     * </ol>
     * </p>
     * @return 작업 성공 여부가 담긴 Response 객체
     * @throws RuntimeException 파일 다운로드, 변환 또는 DB 저장 중 오류 발생 시
     */
    @PostMapping("/api/v1/bus-stops/route")
    public Response<String> downloadAndSaveBusRoute() {
        // csv파일 db에 저장하기
        try {
            busRouteService.saveBusRouteFile("bus_route.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Response.ok("버스 노선 경로 파일 다운로드 -> XLSX 파일 CSV로 변환 -> DB에 저장 성공");
    }
}
