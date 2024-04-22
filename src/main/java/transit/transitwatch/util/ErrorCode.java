package transit.transitwatch.util;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST("4001", "잘못된 요청입니다."),
    FORBIDDEN("4031", "액세스가 금지되었습니다."),
    INTERNAL_SERVER_ERROR("5001", "서버 내부 오류"),
    API_REQUEST_FAIL("5002", "공공 API에서 데이터를 가져오지 못했습니다."),
    FILE_DOWNLOAD_FAIL("5003", "파일 다운로드에 실패했습니다."),
    FILE_NOT_CORRECT("5004", "다운로드 받은 파일 사이즈가 비정상 입니다."),
    PARSING_FAIL("5005", "파싱에 실패했습니다."),
    AUTOCOMPLETE_FAIL("5006", "자동완성 검색어 로딩 중 오류가 발생했습니다."),
    AUTOCOMPLETE_BASEDATA_FAIL("5007", "자동완성 검색어 Base Data 만드는 중 오류가 발생했습니다."),
    GET_URL_FAIL("5008", "URL을 가져오는데 실패했습니다."),
    FILE_SAVE_FAIL("5009", "파일 저장 중 오류가 발생했습니다."),
    SEARCH_FAIL("5010", "조회 중 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE("5031", "일시적인 서버 오류");

    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}

