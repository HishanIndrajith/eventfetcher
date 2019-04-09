package net.codegen.models.event_brite_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationEventBriteAPI
{
	private String latitude;
	private String within;
	private String longitude;
	private String address;

	public LocationEventBriteAPI()
	{
		//Default constructor
	}

	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude( String latitude )
	{
		this.latitude = latitude;
	}

	public String getWithin()
	{
		return within;
	}

	public void setWithin( String within )
	{
		this.within = within;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude( String longitude )
	{
		this.longitude = longitude;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress( String address )
	{
		this.address = address;
	}
}
