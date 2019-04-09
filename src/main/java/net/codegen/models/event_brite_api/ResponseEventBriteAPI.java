package net.codegen.models.event_brite_api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseEventBriteAPI
{
	private PaginationEventBriteAPI pagination;
	private List<EventEventBriteAPI> events;
	private LocationEventBriteAPI location;

	private static int cityIndex;
	// 0 if Melbourne and 1 if Brisbane

	public ResponseEventBriteAPI()
	{
	}

	public ResponseEventBriteAPI( PaginationEventBriteAPI pagination, List<EventEventBriteAPI> events,
			LocationEventBriteAPI location )
	{
		this.pagination = pagination;
		this.events = events;
		this.location = location;
	}

	public PaginationEventBriteAPI getPagination()
	{
		return pagination;
	}

	public void setPagination( PaginationEventBriteAPI pagination )
	{
		this.pagination = pagination;
	}

	public List<EventEventBriteAPI> getEvents()
	{
		return events;
	}

	public void setEvents( List<EventEventBriteAPI> events )
	{
		this.events = events;
	}

	public LocationEventBriteAPI getLocation()
	{
		return location;
	}

	public void setLocation( LocationEventBriteAPI location )
	{
		this.location = location;
	}

	/*
	getCityIndex is used to change the city variable of events based on the value of this. need to change the static
	value using setCityIndex before each request to a new city.
	*/
	public static int getCityIndex()
	{
		return cityIndex;
	}

	public static void setCityIndex( int cityIndex )
	{
		ResponseEventBriteAPI.cityIndex = cityIndex;
	}
}
