package transit.transitwatch.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 트라이 자료구조를 활용하여 한글 문자열의 자동완성 기능을 제공하는 클래스.
 * 이 클래스는 한글, 숫자, 영문자를 포함한 자동완성을 지원한다.
 */
public class TrieNode {
    private TrieNode[] children;
    private boolean isEndWord;
    private Set<String> words = new HashSet<>(); // 이 노드에서 끝나는 모든 station_id를 저장

    public TrieNode() {
        this.children = new TrieNode[105]; //19 + 21 + 28 + 1 , 10, 26 / 한글 자음, 모음, 받침 및 숫자, 영문자를 포함
        this.isEndWord = false;
    }

    private static final char[] ALL = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
            , 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
            , '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
            , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

//    public void insert(String word, String keyword) {
//        TrieNode current = this;
//        for (char ch : word.toCharArray()) {
//            ArrayList<Integer> indexs = HangulUtil.oneCharacter(ch);
//            for (int index : indexs) {
//                if (index == -1) continue;
//                if (current.children[index] == null) {
//                    current.children[index] = new TrieNode();
//                }
//                current = current.children[index];
//                current.words.add(keyword); // 현재 접두사에 대한 단어 추가
//            }
//        }
//        current.isEndWord = true;
//    }

//    public void insertWord(String keyword) {
//        List<String> morpheme = HangulUtil.getMorpheme(keyword);
//        for (String word : morpheme) {
//            insert(word, keyword);
//        }
//    }

    /**
     * 주어진 키워드와 정류장 ID를 트라이에 삽입한다.
     * @param keyword 트라이에 삽입할 키워드
     * @param stationId 키워드에 해당하는 정류장 ID
     */
    public void insertWord(String keyword, String stationId) {
        TrieNode current = this;
        for (char ch : keyword.toCharArray()) {
            ArrayList<Integer> indexs = HangulUtil.oneCharacter(ch);
            for (int index : indexs) {
                if (index == -1) continue;
                if (current.children[index] == null) {
                    current.children[index] = new TrieNode();
                }
                current = current.children[index];
                current.words.add(stationId); // 현재 접두사에 대한 stationId 추가
            }
        }
        current.isEndWord = true;
    }

    /**
     * 주어진 접두사로 시작하는 모든 단어를 찾아 반환한다.
     * @param prefix 검색할 접두사
     * @return 접두사로 시작하는 모든 단어의 집합
     */
    public Set<String> autocomplete(String prefix) {

        TrieNode current = this;
        for (char ch : prefix.toCharArray()) {
            ArrayList<Integer> indexs = HangulUtil.oneCharacter(ch);
            for (int index : indexs) {
                if (index == -1 || current.children[index] == null) {
                    return new HashSet<>();
                }
                current = current.children[index];
            }
        }
        return findAllWords(current, prefix);
    }

    /**
     * 특정 노드에서 시작하는 모든 단어를 찾아 반환하는 재귀 함수.
     * @param node 시작 노드
     * @param prefix 현재까지의 접두사
     * @return 주어진 노드에서 시작하는 모든 단어의 집합
     */
    private Set<String> findAllWords(TrieNode node, String prefix) {
        List<String> words = new ArrayList<>();

        if (node.isEndWord) {
            char[] chars = HangulUtil.separateHangul(prefix);
            String s = HangulUtil.combineJaso(chars);
            words.add(s);
        }
        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                char ch = ALL[i];

                words.addAll(findAllWords(node.children[i], prefix + ch));
            }
        }
        return node.words;
    }
}