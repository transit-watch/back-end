package transit.transitwatch.util;

import org.junit.jupiter.api.Test;

public class HangulTest {

    @Test
    void testHangul() {
        TrieNode trie = new TrieNode();

        trie.insertWord("신서고등학교.영상고등학교입구","1");
        trie.insertWord("신사역","12");
        trie.insertWord("증11산역" ,"2");
        trie.insertWord("양지211말","3");
        trie.insertWord("대덕동행정A복지센터","4");
        trie.insertWord("감자고등학교","5");
        trie.insertWord("사과","6");

        System.out.println("시작하는 단어: " + trie.autocomplete("신ㅅ"));
    }
    /*
    한글 자모의 배열 순서 : https://www.korean.go.kr/nkview/news/12/128.htm
    자음(19자) : ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ
    모음(21자) : ㅏ ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ ㅣ
    받침(27자) : ㄱ ㄲ ㄳ ㄴ ㄵ ㄶ ㄷ ㄹ ㄺ ㄻ ㄼ ㄽ ㄾ ㄿ ㅀ ㅁ ㅂ ㅄ ㅅ ㅆ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ

    한글이 가지는 유니코드 값은 AC00부터 D7A3까지며, 총 11,172개의 코드로 모든 한글을 표현할 수 있다.
    어떤 한글 글자의 십진수 유니코드 = [{(초성)×588}+{(중성)×28}+(종성)]+44032
     */
/*
    private static final int START_CODE = 0xAC00;   // '가' 유니코드 16진수
    private static final int END_CODE = 0xD7A3;     // '힣' 유니코드 16진수
    private static final int CHOSUNG = 588;
    private static final int JUNGSUNG = 28;

    private static final char[] CHOSUNG_LIST = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char[] JUNGSUNG_LIST = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    private static final char[] JONGSUNG_LIST = {
            '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    // 한글 자모 분리
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

    private static int getUnicodeValue(char grapheme) {
        int unicodeVal = grapheme - START_CODE;
        return unicodeVal;
    }

    private static int getJongsungIndex(int unicodeValue) {
        int jongsungIndex = unicodeValue % JUNGSUNG;
        return jongsungIndex;
    }

    private static int getJungsungIndex(int unicodeValue, int chosungIndex) {
        int jungsungIndex = (unicodeValue - (CHOSUNG * chosungIndex)) / JUNGSUNG;
        return jungsungIndex;
    }

    private static int getChosungIndex(int unicodeValue) {
        int chosungIndex = unicodeValue / CHOSUNG;
        return chosungIndex;
    }

    public static void main(String[] args) {
        String original = "ㅗㅎㄹㅇㄴㅎ홋 가각핳 ㄱㄴ";
        char[] decomposed = separateHangul(original);
        System.out.println("원본 문자열: " + original);
        System.out.println("분리된 문자열: " + Arrays.toString(decomposed));
    }
    */
}
