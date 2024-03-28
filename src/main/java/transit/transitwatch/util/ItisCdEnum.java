package transit.transitwatch.util;

import lombok.Getter;

/**
 * 혼잡도 여부 ENUM class
 * 1545：승강장혼잡알림 - 보통
 * 1546：승강장혼잡알림 - 혼잡
 */
@Getter
public enum ItisCdEnum {
    CROWD("1546"),
    NORMAL("1545"),
    EASYGOING("1544");

    private final String code;

    ItisCdEnum(String code) {
        this.code = code;
    }
}
