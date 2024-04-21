package transit.transitwatch.util;

/**
 * 버스 노선 정보 CSV 파일의 헤더 ENUM
 * <p>이 ENUM은 CSV 파일 내에서 버스 노선 정보의 각 컬럼을 나타낸다.</p>
 */
public enum BusRouteEnumHeader {
    ROUTE_ID,
    ROUTE_NAME,
    ROUTE_ORDER,
    STATION_ID,
    ARS_ID,
    STATION_NAME,
    Y_LATITUDE,
    X_LONGITUDE
}
