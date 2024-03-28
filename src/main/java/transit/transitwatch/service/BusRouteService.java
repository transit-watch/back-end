package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.dto.BusRouteDTO;
import transit.transitwatch.entity.BusRoute;
import transit.transitwatch.repository.BusRouteRepository;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.BusRouteEnumHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;
    private final ApiUtil apiUtil;

    @Value("${app.file.path}")
    private String filePath;

    /*
     * 버스 노선 정보 insert
     * */
    @Transactional
    public void saveBusRouteFile(String fileName) throws Exception {

        // 현재 3월 기준 파일 아이디가 26이라 한달에 1씩 증가 시키기. 업로드 주기 첫 주
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonth().getValue();
        int fileId = currentMonth + 23;

        // 서울시 버스 노선별 정류소 정보 파일 다운로드 URL
        String busRouteUrl = "https://datafile.seoul.go.kr/bigfile/iot/inf/nio_download.do?infId=OA-1095&useCache=false&infSeq=2" +
                "&seqNo=" + fileId +
                "&seq=" + fileId;

        // 파일 다운로드
        apiUtil.fileDownload(busRouteUrl, "bus_route.xlsx");

        // 파일 변환하기 (엑셀->csv)
        apiUtil.convertXlsxToCSV(filePath, "bus_route");

        // 저장하기
        Path readPath = Paths.get(filePath + fileName);
        List<BusRoute> busRouteList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(readPath, Charset.forName("UTF-8"))) {

            Iterable<CSVRecord> records = apiUtil.getCsvRecords(br, BusRouteEnumHeader.class);

            for (CSVRecord record : records) {
                String routeId = record.get(BusRouteEnumHeader.ROUTE_ID);
                String routeName = record.get(BusRouteEnumHeader.ROUTE_NAME);
                int routeOrder = Integer.parseInt(record.get(BusRouteEnumHeader.ROUTE_ORDER));
                String stationId = record.get(BusRouteEnumHeader.STATION_ID);
                String arsId = record.get(BusRouteEnumHeader.ARS_ID);
                double xLatitude = Double.parseDouble(record.get(BusRouteEnumHeader.X_LATITUDE));
                double yLongitude = Double.parseDouble(record.get(BusRouteEnumHeader.Y_LONGITUDE));

                BusRouteDTO busRouteDTO = new BusRouteDTO(routeId, routeName, routeOrder, stationId, arsId, xLatitude, yLongitude);
                BusRoute busRoute = busRouteDTO.toEntity();
                busRouteList.add(busRoute);
            }
            busRouteRepository.saveAll(busRouteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
