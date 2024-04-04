package transit.transitwatch.util;

import lombok.Getter;

/**
 * 버스 정류장의 혼잡도 상태를 나타내는 ENUM.
 * code2: 버스 도착정보의 혼잡도
 * 각 혼잡도 상태는 고유한 코드를 가지며, 혼잡도에 따라 다음과 같이 구분됩니다:
 * <ul>
 *     <li>CROWD: 혼잡 - '1546'</li>
 *     <li>NORMAL: 보통 - '1545'</li>
 *     <li>EASYGOING: 여유 - '1544'</li>
 *     <li>NODATA: 데이터 없음 - '0'</li>
 * </ul>
 */
@Getter
public enum ItisCdEnum {
    CROWD("1546", "5", 5),
    NORMAL("1545", "4", 4),
    EASYGOING("1544", "3", 3),
    NODATA("0", "0", 0);

    private final String code;  // 정류장 혼잡도
    private final String code2; // 버스 도착정보 혼잡도
    private final int code3;

    ItisCdEnum(String code, String code2, int code3) {
        this.code = code;
        this.code2 = code2;
        this.code3 = code3;
    }
}
