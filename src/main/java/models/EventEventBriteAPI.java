package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventEventBriteAPI
{
	private String city;
	private String name;
	private String description;
	private String url;
	private String startDate; //UTC
	private String endDate;  //UTC
	private String venueLatitude;
	private String venueLongitude;
	private String venueAddress;

	public EventEventBriteAPI()
	{
		//Default Constructor
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("name")
	private void setName( Map<String, Object> name )
	{
		this.name = ( String ) name.get( "text" );
	}

	public void setCity( String city )
	{
		this.city = city;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("description")
	public void setDescription( Map<String, Object> description )
	{
		this.description = ( String ) description.get( "text" );
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("url")
	public void setUrl( String url )
	{
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("start")
	public void setStartDate( Map<String, Object> start )
	{
		this.startDate = ( String ) start.get( "utc" );
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("end")
	public void setEndDate( Map<String, Object> end )
	{
		this.endDate = ( String ) end.get( "utc" );
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("venue")
	public void setVenue( Map<String, Object> venue )
	{
		this.venueLatitude = ( String ) venue.get( "latitude" );
		this.venueLongitude = ( String ) venue.get( "latitude" );
		Map<String, String> address = ( Map<String, String> ) venue.get( "address" );
		this.venueAddress = address.get( "localized_address_display" );
	}

	public String getCity()
	{
		return city;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getUrl()
	{
		return url;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public String getVenueLatitude()
	{
		return venueLatitude;
	}

	public String getVenueLongitude()
	{
		return venueLongitude;
	}

	public String getVenueAddress()
	{
		return venueAddress;
	}
}
