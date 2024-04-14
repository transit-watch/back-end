package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.service.AutocompleteService;

import java.util.Set;

/**
 * 자동완성 컨트롤러.
 * 정류장명에 대한 자동완성 검색을 처리하며, Redis에 저장된 데이터를 활용한다.
 */
@RequiredArgsConstructor
@RestController
public class AutocompleteController {

    private final AutocompleteService autocompleteService;

    /**
     * 사용자가 입력한 키워드로 정류장명 자동완성 목록을 조회한다.
     * @param prefix 사용자가 입력한 검색어 접두사
     * @return 정류장명 자동완성 목록이 담긴 Response 객체
     */
    @GetMapping("/api/v1/bus-stops/autocomplete/{prefix}")
    public Response<Set<String>> searchBusStopInfo(@PathVariable String prefix) {
        Set<String> autocompleteStation = autocompleteService.getAutocomplete(prefix, "autocomplete:station");
//        Set<String> autocompleteRoute = autocompleteService.getAutocomplete(prefix, "autocomplete:route");
        return Response.ok(autocompleteStation);
    }

    /**
     * Redis에 정류장명에 대한 자동완성 데이터를 세팅한다.
     * MySQL 에서 정류장명을 가져와 Redis에 로드하는 작업을 수행한다.
     * @return 작업 성공 메시지가 담긴 Response 객체
     */
    @GetMapping("/api/v1/bus-stops/autocomplete")
    public Response<String> searchBusStopInfo() {
        autocompleteService.loadStationNameMysqlToRedis();
        return Response.ok("Redis 자동완성 정류장 명 키 값 세팅 성공");
    }
}
