package transit.transitwatch.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.repository.BusStopInfoRepository;
import transit.transitwatch.util.TrieNode;

import java.util.List;

/**
 * 버스 정류장명을 검색하기 위해 트라이에 초기화하고 관리하는 서비스 클래스.
 */
@Slf4j
@Getter
@Component
public class TrieService {
    private final TrieNode trie = new TrieNode();
    private final BusStopInfoRepository busStopInfoRepository;

    /**
     * BusStopInfoRepository를 참조하여 TrieService를 생성한다.
     * 객체 생성 시 트라이를 초기화한다.
     *
     * @param busStopInfoRepository 버스 정류장 정보를 제공하는 레포지토리
     */
    public TrieService(BusStopInfoRepository busStopInfoRepository) {
        this.busStopInfoRepository = busStopInfoRepository;
        initializerTrie();
    }

    /**
     * 버스 정류장 정보를 트라이에 초기화하는 메소드. 모든 버스 정류장 정보를 조회하여 트라이에 삽입한다.
     * 이 메소드는 비동기적으로 실행되며, 실행 시간을 로그로 기록한다.
     */
    @Async
    public void initializerTrie() {
        long startTime = System.currentTimeMillis();
        log.info("Trie start");

        List<BusStopInfo> all = busStopInfoRepository.findAll();
        all.forEach(busStop -> trie.insertWord(busStop.getStationName(), busStop.getStationId()));

        long endTime = System.currentTimeMillis();
        log.info("Trie loading time : {} ms", (endTime - startTime));
    }
}