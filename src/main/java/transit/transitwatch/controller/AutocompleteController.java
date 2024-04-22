package transit.transitwatch.controller;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.SearchKeywordDTO;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.service.AutocompleteService;

import java.util.List;
import java.util.Set;

/**
 * 자동완성 컨트롤러.
 * 정류장명에 대한 자동완성 검색을 처리하며, Redis에 저장된 데이터를 활용한다.
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
public class AutocompleteController {

    private final AutocompleteService autocompleteService;


    @GetMapping("/api/v1/bus-stops/autocomplete")
    public Response<List<SearchKeywordDTO>> searchBusStopInfo(
            @RequestParam("keyword") @Pattern( message = "한글, 영어, 숫자 1 ~ 30자 만 가능합니다.",
                    regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]{1,30}+$") String keyword,
            @RequestParam("yLatitude") @Digits(integer=3, fraction=13) String yLatitude,
            @RequestParam("xLongitude") @Digits(integer=3, fraction=13) String xLongitude) {
        Set<String> autocomplete = autocompleteService.autocomplete(keyword);
//        List<SearchKeywordDTO> result = autocompleteService.searchAndSort(autocomplete, yLatitude, xLongitude);
        List<SearchKeywordDTO> result = autocompleteService.searchAndSortHash(autocomplete, yLatitude, xLongitude);
        return Response.ok(result);
    }

    /**
     * Redis에 정류장명에 대한 자동완성 데이터를 세팅한다.
     * MySQL 에서 정류장명을 가져와 Redis에 로드하는 작업을 수행한다.
     * @return 작업 성공 메시지가 담긴 Response 객체
     */
    @PostMapping("/api/v1/bus-stops/autocomplete")
    public Response<String> saveBusStopInfoRedis() {
//        autocompleteService.createIndex();
        autocompleteService.loadBusStopInfoMysqlToRedis();
        return Response.ok("Redis 자동완성 정류장 명 키 값 세팅 성공");
    }
}
