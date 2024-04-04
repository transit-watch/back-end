package transit.transitwatch.service;


import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.repository.BusStopInfoRepository;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.BusStopInfoEnumHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BusStopInfoService {

    private final BusStopInfoRepository busStopInfoRepository;
    private final ApiUtil apiUtil;

    @Value("${app.file.path}")
    private String filePath;


    /*
     * 버스 정류소 정보 insert
     * */
    @Transactional
    public void saveBusStopInfoFile(String fileName) throws Exception {

        // 정류장 정보 파일 다운로드 URL
        String busStopInfoAddress = "https://t-data.seoul.go.kr/dataprovide/download.do?id=10257";
        apiUtil.fileDownload(busStopInfoAddress, "bus_stop_info.csv");

        // 저장하기
        Path readPath = Paths.get(filePath + fileName);
        List<BusStopInfo> busStopInfoList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(readPath, Charset.forName("UTF-8"))) {
            Iterable<CSVRecord> records = apiUtil.getCsvRecords(br, BusStopInfoEnumHeader.class);

            for (CSVRecord record : records) {
                String stationId = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.STATION_ID));
                String stationName = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.STATION_NAME));
                String arsId = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.ARS_ID));
                String linkId = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.LINK_ID));
                Double xLatitude = Double.parseDouble(apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.X_LATITUDE)));
                Double yLongitude = Double.parseDouble(apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.Y_LONGITUDE)));
                char useYn = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.USE_YN)).charAt(0);
                char virtualBusStopYn = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.VIRTUAL_BUS_STOP_YN)).charAt(0);

                // 가상정류장 제외, 미사용 제외
                if (useYn == '0') continue;
                if (virtualBusStopYn == '1') continue;

                busStopInfoRepository.upsertBusStopInfo(stationId, stationName, arsId, linkId, xLatitude, yLongitude, useYn, virtualBusStopYn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BusStopInfo selectBusStopArsId(String arsId) {
        return busStopInfoRepository.findByArsId(arsId);
    }
}
