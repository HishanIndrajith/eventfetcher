package net.codegen.models.event_ful_api;

import net.codegen.models.Event;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "event")
@XmlRootElement(name = "events")
@XmlAccessorType(XmlAccessType.NONE)
public class EventEventFulAPI extends Event
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eventId;
	private String city;
	@Column(length = 1000)
	@XmlElement(name = "title")
	private String name;
	@XmlElement(name = "url")
	private String url;
	@Lob
	@XmlElement(name = "description")
	private String description;
	@XmlAttribute(name = "start_time")
	private String startDate;
	@XmlElement(name = "stop_time")
	private String endDate;
	@XmlElement(name = "latitude")
	private String venueLatitude;
	@XmlElement(name = "longitude")
	private String venueLongitude;
	@XmlElement(name = "venue_address")
	private String venueAddress;

	public void setName( String name )
	{
		this.name = name;
	}

	public void setCity( String city )
	{
		this.city = city;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public void setStartDate( String startDate )
	{
		this.startDate = startDate;
	}

	public void setEndDate( String endDate )
	{
		this.endDate = endDate;
	}

	public void setVenueLatitude( String venueLatitude )
	{
		this.venueLatitude = venueLatitude;
	}

	public void setVenueLongitude( String venueLongitude )
	{
		this.venueLongitude = venueLongitude;
	}

	public void setVenueAddress( String venueAddress )
	{
		this.venueAddress = venueAddress;
	}

	public String getName()
	{
		return name;
	}

	public String getUrl()
	{
		return url;
	}

	public String getDescription()
	{
		return description;
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

	public String getCity()
	{
		return city;
	}
}
