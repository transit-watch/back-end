package transit.transitwatch.service;

public interface BusStopInfoService {
    void saveBusStopInfoFile(String fileName) throws Exception;
    void truncate() throws Exception;
}
