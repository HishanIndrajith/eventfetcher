package net.codegen.models;

import javax.persistence.*;
import java.util.Map;

@Entity
public abstract class Event
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int eventId;
	private String city;
	private String name;
	@Column(length = 10000)
	private String description;
	private String url;
	private String startDate; //UTC
	private String endDate;  //UTC
	private String venueLatitude;
	private String venueLongitude;
	private String venueAddress;

	public Event()
	{
		//Default constructor
	}

	public abstract void setEventId( int eventId );

	public abstract void setName( Map<String, Object> name );

	public abstract void setDescription( Map<String, Object> description );

	public abstract void setStartDate( Map<String, Object> start );

	public abstract void setEndDate( Map<String, Object> end );

	public abstract void setVenue( Map<String, Object> venue );

	public abstract String getCity();

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getUrl();

	public abstract String getStartDate();

	public abstract String getEndDate();

	public abstract String getVenueLatitude();

	public abstract String getVenueLongitude();

	public abstract String getVenueAddress();

	public abstract int getEventIdId();
}
