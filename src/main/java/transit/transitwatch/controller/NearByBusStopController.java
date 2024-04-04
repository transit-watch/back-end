package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.near.ItemNear;
import transit.transitwatch.dto.response.NearByBusStopResponse;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.service.NearByBusStopService;

import java.util.List;

/**
 * 사용자의 위치 주변에 있는 버스 정류장 정보 제공 컨트롤러.
 */
@RequiredArgsConstructor
@RestController
public class NearByBusStopController {

    private final NearByBusStopService nearByBusStopService;

    /**
     * 사용자의 현재 좌표와 지정된 반경 내에 있는 버스 정류장 목록을 조회한다.
     * <p>이 메서드는 사용자의 위치 좌표와 검색 반경을 파라미터로 받아 해당 범위 내에 있는
     * 버스 정류장 정보를 조회하여 반환한다.</p>
     * @param tmX 사용자의 x 좌표 (위도)
     * @param tmY 사용자의 y 좌표 (경도)
     * @param radius 검색 반경(미터 단위)
     * @return 근처 버스 정류장 목록이 담긴 Response 객체
     * @throws RuntimeException API 호출 중 예외 발생 시
     */
    @GetMapping("/api/v1/bus-stops/near")
    public Response<List<NearByBusStopResponse>> nearByBusStopList(@RequestParam("tmX") double tmX
            , @RequestParam("tmY") double tmY, @RequestParam("radius") int radius) {

        CommonApiDTO<ItemNear> commonApiDTO = null;

        // api에서 근처 정류장 정보 가져오기
        try {
            commonApiDTO = nearByBusStopService.getNearByBusStopApi(tmX, tmY, radius);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<NearByBusStopResponse> collect = nearByBusStopService.getNearByBusStopResponses(commonApiDTO);

        return Response.ok(collect);
    }
}
