package net.codegen.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class CityListFetcher
{
	JsonParser jsonParser = new JacksonJsonParser();
	private List<String> cityList;
	private static final String CITIES_FILE_NAME = "cities.json";

	public List<String> getRequiredCityList()
	{
		try
		{
			ClassLoader classLoader = getClass().getClassLoader();
			ObjectMapper objectMapper = new ObjectMapper();
			InputStream input = classLoader.getResourceAsStream( CITIES_FILE_NAME );
			cityList = objectMapper.readValue( input, List.class );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}

		return cityList;
	}

}
