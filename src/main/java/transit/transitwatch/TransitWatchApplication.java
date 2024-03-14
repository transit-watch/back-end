package transit.transitwatch;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootApplication
public class TransitWatchApplication {


	public static void main(String[] args) {
		SpringApplication.run(TransitWatchApplication.class, args);
	}

}
