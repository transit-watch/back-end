package transit.transitwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TransitWatchApplication {


	public static void main(String[] args) {
		SpringApplication.run(TransitWatchApplication.class, args);
	}

}
