package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.dto.BusRouteDTO;
import transit.transitwatch.dto.BusStopInfoDTO;
import transit.transitwatch.entity.BusRoute;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.repository.BusRouteRepository;
import transit.transitwatch.repository.BusStopInfoRepository;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.BusRouteEnumHeader;
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
public class ApiServiceImpl implements ApiService{
    private final BusRouteRepository busRouteRepository;
    private final BusStopInfoRepository busStopInfoRepository;

    /*
     * 버스 노선 정보 insert
     * */
    @Transactional
    public void busRouteXLSXFileSave(String fileName) throws Exception{
        String fileDir = "/Users/yeyoung/Desktop/api 데이터 파일/";

        Path readPath = Paths.get(fileDir + fileName);
        List<BusRoute> busRouteList = new ArrayList<>();

        ApiUtil apiUtil = new ApiUtil();

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
                BusRoute busRoute = new BusRoute();
                busRoute.setBusRouteDTO(busRouteDTO);
                busRouteList.add(busRoute);
            }
            busRouteRepository.saveAll(busRouteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 버스 정류소 정보 insert
     * */
    @Transactional
    public void busStopInfoCSVFileSave(String fileName) throws Exception{
        String fileDir = "/Users/yeyoung/Desktop/api 데이터 파일/";

        Path readPath = Paths.get(fileDir + fileName);
        List<BusStopInfo> busStopInfoList = new ArrayList<>();

        ApiUtil apiUtil = new ApiUtil();

//        try (
//                InputStream inputStream = Files.newInputStream(readPath);
//
//                BOMInputStream bomInputStream = new BOMInputStream.Builder()
//                        .setInputStream(inputStream)
//                        .setByteOrderMarks(ByteOrderMark.UTF_8)
//                        .setInclude(false)
//                        .get();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(bomInputStream, StandardCharsets.UTF_8))
//        ) {
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
                if(useYn == '0') continue;
                if(virtualBusStopYn == '1') continue;

                BusStopInfoDTO busStopInfoDTO = new BusStopInfoDTO(stationId, stationName, arsId, linkId, xLatitude, yLongitude, useYn, virtualBusStopYn);
                BusStopInfo busStopInfo = new BusStopInfo();
                busStopInfo.setBusStopInfoDTO(busStopInfoDTO);
                busStopInfoList.add(busStopInfo);
            }
            busStopInfoRepository.saveAll(busStopInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
