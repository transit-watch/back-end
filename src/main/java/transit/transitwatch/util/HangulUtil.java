package transit.transitwatch.util;


import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * 한글 문자와 관련된 다양한 유틸리티 기능을 제공하는 클래스.
 * 한글 자모를 분리하고 조합하는 기능을 포함하여, 한글 문자 처리에 필요한 여러 메소드를 포함한다.
 */
public class HangulUtil {

    /*
    한글 자모의 배열 순서 : https://www.korean.go.kr/nkview/news/12/128.htm
    자음(19자) : ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ
    모음(21자) : ㅏ ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ ㅣ
    받침(27자) : ㄱ ㄲ ㄳ ㄴ ㄵ ㄶ ㄷ ㄹ ㄺ ㄻ ㄼ ㄽ ㄾ ㄿ ㅀ ㅁ ㅂ ㅄ ㅅ ㅆ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ

    한글이 가지는 유니코드 값은 AC00부터 D7A3까지며, 총 11,172개의 코드로 모든 한글을 표현할 수 있다.
    어떤 한글 글자의 십진수 유니코드 = [{(초성)×588}+{(중성)×28}+(종성)]+44032
     */

    private static final int START_CODE = 0xAC00;   // '가' 유니코드 16진수
    private static final int END_CODE = 0xD7A3;     // '힣' 유니코드 16진수
    private static final int CHOSUNG = 588;
    private static final int JUNGSUNG = 28;

    private static final int NUM_START = 0x30; // '0'의 유니코드
    private static final int NUM_END = 0x39;   // '9'의 유니코드
    private static final int ENG_START = 0x61; // 'a'의 유니코드
    private static final int ENG_END = 0x7A;   // 'z'의 유니코드

    private static final char[] CHOSUNG_LIST = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char[] JUNGSUNG_LIST = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    private static final char[] JONGSUNG_LIST = {
            '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    /**
     * 주어진 키워드에 대해 형태소 분석을 수행하고 결과 리스트를 반환한다.
     * @param keyword 분석할 한글 키워드
     * @return 형태소로 분리된 결과를 담은 문자열 리스트
     */
    public static List<String> getMorpheme(String keyword) {
        List<String> morphemeList = new ArrayList<>();
        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        KomoranResult analyzeResultList = komoran.analyze(keyword);
        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token morpheme : tokenList) {
            morphemeList.add(morpheme.getMorph());
        }
        return morphemeList;
    }

    /**
     * 분리된 자모를 조합하여 완성형 한글 문자로 반환한다.
     * @param separatedJaso 분리된 자모 배열
     * @return 조합된 완성형 한글 문자열
     */
    public static String combineJaso(char[] separatedJaso) {
        StringBuilder result = new StringBuilder();
        int startIdx = 0;

        while (startIdx < separatedJaso.length - 1) {
            int chosungIndex = findIndex(CHOSUNG_LIST, separatedJaso[startIdx]);
            int jungsungIndex = findIndex(JUNGSUNG_LIST, separatedJaso[startIdx + 1]);
            int jongsungIndex = 0;

            int size = getSize(separatedJaso, startIdx);

            if (size == 3) {
                jongsungIndex = findIndex(JONGSUNG_LIST, separatedJaso[startIdx + 2]);
            }

            int unicodeValue = 0xAC00 + (chosungIndex * 21 * 28) + (jungsungIndex * 28) + jongsungIndex;

            result.append((char) unicodeValue);
            startIdx += size;
        }

        return result.toString();
    }

    /**
     * 지정된 시작 인덱스부터의 자모 배열 크기를 계산한다.
     * @param separatedJaso 분리된 자모 배열
     * @param startIdx 시작 인덱스
     * @return 조합 가능한 자모의 크기
     */
    public static int getSize(char[] separatedJaso, final int startIdx) {
        int length = separatedJaso.length;
        int remainJasoLength = length - startIdx;
        int assembleSize = 0;

        if (remainJasoLength > 3) {
            char c = separatedJaso[startIdx + 3];
            if (new String(JUNGSUNG_LIST).contains(String.valueOf(c))) {
                assembleSize = 2;
            } else {
                assembleSize = 3;
            }
        } else if (remainJasoLength == 3 || remainJasoLength == 2) {
            assembleSize = remainJasoLength;
        }

        return assembleSize;
    }

    /**
     * 주어진 대상 문자에 해당하는 리스트 내의 인덱스를 반환한다.
     * @param list 탐색할 문자 배열
     * @param target 찾을 문자
     * @return 대상 문자의 인덱스, 찾지 못할 경우 -1 반환
     */
    private static int findIndex(char[] list, char target) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == target) {
                return i;
            }
        }
        return -1; // 못 찾은 경우
    }

    /**
     * 한글 자모를 분리하여 배열로 반환한다.
     * @param str 분리할 한글 문자열
     * @return 분리된 자모를 포함하는 문자 배열
     */
    public static char[] separateHangul(String str) {
        StringBuilder separate = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char grapheme = str.charAt(i);

            if (grapheme >= START_CODE && grapheme <= END_CODE) {
                int unicodeValue = getUnicodeValue(grapheme);
                int chosungIndex = getChosungIndex(unicodeValue);
                int jungsungIndex = getJungsungIndex(unicodeValue, chosungIndex);
                int jongsungIndex = getJongsungIndex(unicodeValue);

                separate.append(CHOSUNG_LIST[chosungIndex]);
                separate.append(JUNGSUNG_LIST[jungsungIndex]);
                if (jongsungIndex != 0) {
                    separate.append(JONGSUNG_LIST[jongsungIndex]);
                }
            } else {
                separate.append(grapheme);
            }
        }
        return separate.toString().toCharArray();
    }

    /**
     * 한 글자의 한글 또는 다른 문자의 인덱스를 반환한다.
     * 인덱스는 한글 초성, 중성, 종성, 숫자, 영어 소문자 순으로 계산된다.
     *
     * @param grapheme 분석할 글자
     * @return 각 글자의 인덱스를 포함하는 ArrayList<Integer>
     */
    public static ArrayList<Integer> oneCharacter(char grapheme) {

        ArrayList<Integer> result = new ArrayList<>();
        String chosungList = new String(CHOSUNG_LIST);
        if (grapheme >= START_CODE && grapheme <= END_CODE) {
            int unicodeValue = getUnicodeValue(grapheme);
            int chosungIndex = getChosungIndex(unicodeValue);
            int jungsungIndex = getJungsungIndex(unicodeValue, chosungIndex);
            int jongsungIndex = getJongsungIndex(unicodeValue);

            result.add(chosungIndex);
            result.add(19 + jungsungIndex);
            if (jongsungIndex != 0)
                result.add(40 + jongsungIndex); // 19 + 21

        } else if (chosungList.contains(String.valueOf(grapheme))) {
            int chosungIndex = chosungList.indexOf(String.valueOf(grapheme));
            result.add(chosungIndex);
        }
        // 숫자 처리
        else if (grapheme >= NUM_START && grapheme <= NUM_END) {
            int index = 68 + (grapheme - NUM_START); // 숫자 인덱스 계산 19 + 21 + 28
            result.add(index);
            // 영어 소문자 처리
        } else if (grapheme >= ENG_START && grapheme <= ENG_END) {
            int index = 78 + (grapheme - ENG_START); // 영어 소문자 인덱스 계산 19 + 21 + 28 + 10
            result.add(index);
        } else {
            result.add(-1);
        }

        return result;
    }
    /**
     * 지정된 한글 글자의 유니코드 값을 계산한다.
     *
     * @param grapheme 계산할 한글 글자
     * @return 유니코드 값의 숫자
     */
    private static int getUnicodeValue(char grapheme) {
        return grapheme - START_CODE;
    }

    /**
     * 유니코드 값에서 종성 인덱스를 추출한다.
     *
     * @param unicodeValue 유니코드 값
     * @return 종성의 인덱스
     */
    private static int getJongsungIndex(int unicodeValue) {
        return unicodeValue % JUNGSUNG;
    }

    /**
     * 유니코드 값에서 종성 인덱스를 추출한다.
     *
     * @param unicodeValue 유니코드 값
     * @return 종성의 인덱스
     */
    private static int getJungsungIndex(int unicodeValue, int chosungIndex) {
        return (unicodeValue - (CHOSUNG * chosungIndex)) / JUNGSUNG;
    }

    /**
     * 유니코드 값에서 초성 인덱스를 추출한다.
     *
     * @param unicodeValue 유니코드 값
     * @return 초성의 인덱스
     */
    private static int getChosungIndex(int unicodeValue) {
        return unicodeValue / CHOSUNG;
    }
}
