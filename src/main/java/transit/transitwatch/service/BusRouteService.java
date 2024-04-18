package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.exception.ServiceException;
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

import static transit.transitwatch.util.ErrorCode.FILE_DOWNLOAD_FAIL;
import static transit.transitwatch.util.ErrorCode.FILE_SAVE_FAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;
    private final ApiUtil apiUtil;

    @Value("${app.file.path}")
    private String filePath;

    /**
     * 지정된 파일 이름으로 버스 노선 정보 파일을 저장한다.
     * 파일 다운로드 실패 시 이전 달의 파일을 시도하고, 그마저 실패하면 ServiceException을 발생시킨다.
     *
     * @param fileName 저장할 파일 이름
     * @throws ServiceException 파일 다운로드 또는 파일 저장 실패 시 발생
     */
    @Transactional
    public void saveBusRouteFile(String fileName){
        // 현재 3월 기준 파일 아이디가 26이라 한달에 1씩 증가. 업로드 주기 첫 주
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonth().getValue();
        int fileId = currentMonth + 23;

        String busRouteUrl = getBusRouteUrl(fileId);
        boolean fileDownloaded = apiUtil.fileDownload(busRouteUrl, "bus_route.xlsx");

        if (!fileDownloaded) {
            busRouteUrl = getBusRouteUrl(fileId - 1);
            fileDownloaded = apiUtil.fileDownload(busRouteUrl, "bus_route.xlsx");
            if (!fileDownloaded) {
                log.error("bus_route.xlsx 파일 다운로드 실패: url={}", busRouteUrl);
                throw new ServiceException(FILE_DOWNLOAD_FAIL);
            }
        }
        apiUtil.convertXlsxToCSV(filePath, "bus_route");

        Path readPath = Paths.get(filePath + fileName);

        try (BufferedReader br = Files.newBufferedReader(readPath, Charset.forName("UTF-8"))) {
            Iterable<CSVRecord> records = apiUtil.getCsvRecords(br, BusRouteEnumHeader.class);

            recordsProcess(records);
        } catch (IOException e) {
            log.error("버스 노선 정보 파일 저장 실패: {}", e.getMessage());
            throw new ServiceException(FILE_SAVE_FAIL);
        }
    }

    /**
     * CSV 레코드를 처리하여 DB에 업데이트한다.
     *
     * @param records 처리할 CSV 레코드
     */
    private void recordsProcess(Iterable<CSVRecord> records) {
        for (CSVRecord record : records) {
            try {
                String routeId = record.get(BusRouteEnumHeader.ROUTE_ID);
                String routeName = record.get(BusRouteEnumHeader.ROUTE_NAME);
                int routeOrder = Integer.parseInt(record.get(BusRouteEnumHeader.ROUTE_ORDER));
                String stationId = record.get(BusRouteEnumHeader.STATION_ID);
                String arsId = record.get(BusRouteEnumHeader.ARS_ID);
                double xLatitude = Double.parseDouble(record.get(BusRouteEnumHeader.X_LATITUDE));
                double yLongitude = Double.parseDouble(record.get(BusRouteEnumHeader.Y_LONGITUDE));

                busRouteRepository.upsertBusRoute(routeId, routeName, routeOrder, stationId, arsId, xLatitude, yLongitude);
            } catch (Exception e) {
                log.error("버스 노선 정보 레코드 파싱 실패: {}", record, e);
            }
        }
    }

    /**
     * 버스 노선 정보 파일 다운로드 URL을 생성한다.
     *
     * @param fileId 파일 ID
     * @return 생성된 URL
     */
    private String getBusRouteUrl(int fileId) {
        // 서울시 버스 노선별 정류소 정보 파일 다운로드 URL
        return String.format("https://datafile.seoul.go.kr/bigfile/iot/inf/nio_download.do?infId=OA-1095&useCache=false&infSeq=2&seqNo=%d&seq=%d"
                , fileId, fileId);
    }
}
