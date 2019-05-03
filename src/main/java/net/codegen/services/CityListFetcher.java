package net.codegen.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class Fetch the list of cities from the cities.json file using jackson databind
 */

@Service
public class CityListFetcher
{
	private static final String CITIES_FILE_NAME = "cities.json";
	private static final Logger log = LoggerFactory.getLogger( CityListFetcher.class );

	public List<String> getRequiredCityList()
	{
		List<String> cityList = null;
		InputStream input = null;
		try
		{
			ClassLoader classLoader = getClass().getClassLoader();
			ObjectMapper objectMapper = new ObjectMapper();
			// Get input stream for the json file
			input = classLoader.getResourceAsStream( CITIES_FILE_NAME );
			// Get the city list
			cityList = objectMapper.readValue( input, List.class );
		}
		catch ( IOException e )
		{
			log.error( "Exception in city list fetching {}", e );

		}
		finally
		{
			if ( input != null )
			{
				try
				{
					input.close();
				}
				catch ( IOException e )
				{
					log.error( "{}", e );
				}
			}
		}

		if ( cityList == null )
			return Collections.emptyList();
		else
			return cityList;
	}

}
