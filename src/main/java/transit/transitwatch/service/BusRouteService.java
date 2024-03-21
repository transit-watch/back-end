package transit.transitwatch.service;

public interface BusRouteService {
    void saveBusRouteFile(String fileName) throws Exception;
    void truncate() throws Exception;
}

