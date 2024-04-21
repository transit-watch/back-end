package transit.transitwatch.repository.projections;

public interface SearchKeywordProjection {
    String getStationId();
    String getStationName();
    String getArsId();
    String getYLatitude();
    String getXLongitude();
    String getNextStationName();
}
