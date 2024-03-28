package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transit.transitwatch.entity.view.BusStopCrowdingView;
import transit.transitwatch.repository.projections.BusStopCrowdingProjection;

import java.util.List;
import java.util.Optional;

/*
* 버스 승강장 별 최근 혼잡 정보 뷰 repository
* */
@Repository
public interface BusStopCrowdingViewRepository extends JpaRepository<BusStopCrowdingView, Long> {
    Optional<BusStopCrowdingProjection> findByArsId(String arsId);

    List<BusStopCrowdingProjection> findByArsIdIn(List<String> arsIdList);
}
