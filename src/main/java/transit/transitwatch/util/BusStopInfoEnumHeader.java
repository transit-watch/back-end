package transit.transitwatch.util;

/**
 * 버스 정류소 정보 CSV 파일의 헤더를 정의하는 ENUM
 * <p>이 ENUM은 CSV 파일에서 버스 정류소 정보의 각 컬럼을 나타낸다.</p>
 */
public enum BusStopInfoEnumHeader {
    STATION_ID,
    STATION_NAME,
    BUS_STOP_TYPE,
    ARS_ID,
    BUS_STOP_DESCRIPTION,
    AREA_ID,
    ROUTE_HISTORY_ID,
    DISTANCE,
    LINK_ID,
    HEADING_ANGLE,
    X_LATITUDE,
    Y_LONGITUDE,
    MAPPING_X_COORDINATE,
    MAPPING_Y_COORDINATE,
    VOICE_ID,
    USE_YN,
    BIT_INSTALLATION,
    BUS_STOP_AREA_TYPE,
    VIRTUAL_BUS_STOP_YN,
    BUS_STOP_NAME_ENG,
    BUS_STOP_DETAIL_TYPE,
    BUS_STOP_DIVISION,
    BUS_STOP_BAY
}
