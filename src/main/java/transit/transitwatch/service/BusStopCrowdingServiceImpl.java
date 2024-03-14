package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Service
public class BusStopCrowdingServiceImpl implements BusStopCrowdingService{

    private final RestTemplate restTemplate;

    @Override
    public String getApi(String url) throws Exception {
        String forObject = restTemplate.getForObject(url, String.class);

        return forObject;
    }
}
