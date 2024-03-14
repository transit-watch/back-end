package transit.transitwatch.service;

public interface ApiService {
    void busRouteXLSXFileSave(String fileName) throws Exception;
    void busStopInfoCSVFileSave(String fileName) throws Exception;
}

