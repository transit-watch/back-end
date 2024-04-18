package transit.transitwatch.repository.projections;

public interface SearchKeywordProjection {
    String getStationId();
    String getStationName();
    String getArsId();
    String getXLatitude();
    String getYLongitude();
    String getNextStationName();
}
