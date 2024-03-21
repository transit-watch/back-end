package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.service.ApiService;
import transit.transitwatch.util.ApiUtil;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class ApiController {

    private final ApiUtil apiUtil;
    private final ApiService apiService;

    /*
     * 버스 정류장 정보 파일 다운로드
     * */
    @GetMapping("/api/download/busStop")
    public void downloadBusStopInfoCSV() {
        // 정류장 정보 파일 다운로드 URL
        String busStopInfoAddress = "https://t-data.seoul.go.kr/dataprovide/download.do?id=10257";

        apiUtil.fileDownload(busStopInfoAddress, "bus_stop_info.csv");
    }

    /*
     * 버스 노선 경로 파일 다운로드
     * */
    @GetMapping("/api/download/busRoute")
    public void downloadBusRouteCSV() {
        // 현재 3월 기준 파일 아이디가 26이라 한달에 1씩 증가 시키기. 업로드 주기 첫 주
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonth().getValue();
        ;
        int fileId = currentMonth + 23;

        // 서울시 버스 노선별 정류소 정보 파일 다운로드 URL
        String busRouteAddress = "https://datafile.seoul.go.kr/bigfile/iot/inf/nio_download.do?infId=OA-1095&useCache=false&infSeq=2" +
                "&seqNo=" + fileId +
                "&seq=" + fileId;

        apiUtil.fileDownload(busRouteAddress, "bus_route.xlsx");
    }

    /*
     * 버스 노선 정보 csv파일 db에 저장하기
     * */
    @GetMapping("/api/save/busRoute")
    public void saveBusRouteXLSX() {
        String fileName = "bus_route.csv";
        apiService.busRouteXLSXFileSave(fileName);
    }

    /*
     * 버스 정류장 정보 csv파일 db에 저장하기
     * */
    @GetMapping("/api/save/busStopInfo")
    public void saveBusStopInfoCSV() {
        String fileName = "bus_stop_info.csv";
        apiService.busStopInfoCSVFileSave(fileName);
    }

    /*
    * 버스 노선정보 엑셀파일 -> csv로 변환하기
    * */
    @GetMapping("/api/convert/busRoute")
    public void convertBusRoute() {
        String fileDir = "/Users/yeyoung/Desktop/api 데이터 파일/";
        String fileName = "bus_route";
        apiUtil.convertXlsxToCSV(fileDir, fileName);
    }
}
