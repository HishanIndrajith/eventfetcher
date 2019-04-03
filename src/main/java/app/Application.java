package app;

import models.EventEventBriteAPI;
import models.Quote;
import models.ResponseEventBriteAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
public class Application
{

	private static final Logger log = LoggerFactory.getLogger( Application.class );

	public static void main( String [] args )
	{
		SpringApplication.run( Application.class );
	}

	@Bean
	public RestTemplate restTemplate( RestTemplateBuilder builder )
	{
		return builder.build();
	}

	@Bean
	public CommandLineRunner run( RestTemplate restTemplate ) throws Exception
	{
		return args -> {
			String authToken = "OYPUWKNJCVLUGYVQCPKH";

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever
			headers.add("Authorization", "Bearer "+authToken );

			ResponseEntity<ResponseEventBriteAPI> response = restTemplate.exchange(
					"https://www.eventbriteapi.com/v3/events/search?location.address=melbourne&expand=venue&page=1",
					HttpMethod.GET,
					new HttpEntity<>("parameters", headers),
					ResponseEventBriteAPI.class
			);
			ResponseEventBriteAPI response1 = response.getBody();
			for( EventEventBriteAPI event : response1.getEvents()){
				System.out.println("size = " + event.getSummary());
			}
			log.info( response1.toString() );
		};

	}
}