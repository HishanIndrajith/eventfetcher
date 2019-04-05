package net.codegen.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseEventBriteAPI
{
	private PaginationEventBriteAPI pagination;
	private List <Event> events;
	private LocationEventBriteAPI location;

	public ResponseEventBriteAPI()
	{
	}

	public ResponseEventBriteAPI( PaginationEventBriteAPI pagination, List<Event> events,
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

	public List<Event> getEvents()
	{
		return events;
	}

	public void setEvents( List<Event> events )
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
}
