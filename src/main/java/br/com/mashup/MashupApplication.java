package br.com.mashup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import br.com.mashup.services.GitHubService;
import br.com.mashup.services.MashupService;

@SpringBootApplication
public class MashupApplication implements CommandLineRunner {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(GitHubService.class);
	
	@Autowired
	MashupService mashupService;

	@Value("${mashup.search.query.param}")
	String queryParam;

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(MashupApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setLogStartupInfo(false);;
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Executing mashup service with filter: " + queryParam);

		String jsonResult = mashupService.consolidate(queryParam);

		if (jsonResult != null && !"".equals(jsonResult))
			log.info(jsonResult);
		else
			log.info("Received an empty return from mashup service.");

		System.exit(0);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
