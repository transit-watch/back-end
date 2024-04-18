package transit.transitwatch.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import transit.transitwatch.exception.ServiceException;
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

import static transit.transitwatch.util.ErrorCode.*;

/**
 * 버스 정류소 정보의 다운로드, 파싱 및 DB에 저장하는 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BusStopInfoService {

    private final BusStopInfoRepository busStopInfoRepository;
    private final ApiUtil apiUtil;

    @Value("${app.file.path}")
    private String filePath;

    /**
     * 버스 정류소 정보 파일을 다운로드하고 해당 파일을 파싱하여 DB에 저장한다.
     * 파일 다운로드 실패 시 ServiceException을 발생시킨다.
     *
     * @param fileName 저장할 파일의 이름
     * @throws ServiceException 파일 다운로드 또는 저장 실패 시 발생
     */
    @Transactional
    public void saveBusStopInfoFile(String fileName){
        // 정류장 정보 파일 다운로드 URL
        String busStopInfoAddress = "https://t-data.seoul.go.kr/dataprovide/download.do?id=10257";
        boolean downloadSuccess = apiUtil.fileDownload(busStopInfoAddress, "bus_stop_info.csv");

        if (!downloadSuccess) {
            log.error("버스 정류소 정보 파일 다운로드 실패");
            throw new ServiceException(FILE_DOWNLOAD_FAIL);
        }

        // 저장하기
        Path readPath = Paths.get(filePath + fileName);

        try (BufferedReader br = Files.newBufferedReader(readPath, Charset.forName("UTF-8"))) {
            Iterable<CSVRecord> records = apiUtil.getCsvRecords(br, BusStopInfoEnumHeader.class);
            recordsProcess(records);
        } catch (IOException e) {
            log.error("버스 정류소 정보 파일 저장 실패: {}", e.getMessage());
            throw new ServiceException(FILE_SAVE_FAIL);
        }
    }
    
    /**
     * CSV 레코드를 처리하여 DB에 업데이트한다.
     * 사용하지 않는 레코드는 건너뛴다.
     *
     * @param records 처리할 CSV 레코드
     */
    private void recordsProcess(Iterable<CSVRecord> records) {
        for (CSVRecord record : records) {
            try {
                String stationId = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.STATION_ID));
                String stationName = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.STATION_NAME));
                String arsId = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.ARS_ID));
                String linkId = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.LINK_ID));
                Double xLatitude = Double.parseDouble(apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.X_LATITUDE)));
                Double yLongitude = Double.parseDouble(apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.Y_LONGITUDE)));
                char useYn = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.USE_YN)).charAt(0);
                char busStopAreaType = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.BUS_STOP_AREA_TYPE)).charAt(0);
                char virtualBusStopYn = apiUtil.removeBOM(record.get(BusStopInfoEnumHeader.VIRTUAL_BUS_STOP_YN)).charAt(0);

                if (skipRecord(useYn, busStopAreaType, virtualBusStopYn)) continue;

                busStopInfoRepository.upsertBusStopInfo(stationId, stationName, arsId, linkId, xLatitude, yLongitude, useYn, virtualBusStopYn);
            } catch (Exception e) {
                log.error("버스 정류소 정보 레코드 파싱 실패: {}", record, e);
            }
        }
    }


    /**
     * 제공된 레코드가 DB에 저장될지 여부를 결정한다.
     * 사용하지 않는 정류장('0'), 가상 정류장('1'), 서울 정류장 구역 코드('0' 또는 '-')가 아닐 경우 레코드를 건너뛴다.
     *
     * @param useYn 사용 여부 ('0'은 미사용)
     * @param busStopAreaType 버스 정류장 구역 타입 ('0'은 일반, '-'는 서울 정류장을 의미)
     * @param virtualBusStopYn 가상 정류장 여부 ('1'은 가상 정류장)
     * @return 레코드를 건너뛸지 여부 (true면 건너뛴다)
     */
    private boolean skipRecord(char useYn, char busStopAreaType, char virtualBusStopYn) {
        return useYn == '0' || (busStopAreaType != '0' && busStopAreaType != '-') || virtualBusStopYn == '1';
    }

    /**
     * 특정 ARS ID를 가진 버스 정류장 정보를 조회한다.
     *
     * @param arsId 조회할 ARS ID
     * @return 조회된 버스 정류장 정보
     * @throws ServiceException 정보 조회 실패 시 발생
     */
    public BusStopInfo selectBusStopArsId(String arsId) {
        try {
            return busStopInfoRepository.findByArsId(arsId);
        } catch (Exception e) {
            log.error("ARS ID로 버스 정류장 조회 실패: {}", arsId, e);
            throw new ServiceException(SEARCH_FAIL);
        }
    }
}
