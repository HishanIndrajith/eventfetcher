package net.codegen.models.event_ful_api;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "search")
@XmlAccessorType(XmlAccessType.NONE)
public class ResponseEventFulAPI
{
	@XmlElement(name = "page_count")
	private String pageCount;
	@XmlElement(name = "page_number")
	private String page;
	@XmlElementWrapper(name = "events")
	@XmlElement(name = "event")
	private List<EventEventFulAPI> events;

	private static int cityIndex;
	// 0 if Melbourne and 1 if Brisbane

	public void setPageCount( String pageCount )
	{
		this.pageCount = pageCount;
	}

	public void setPage( String page )
	{
		this.page = page;
	}

	public void setEvents( List<EventEventFulAPI> events )
	{
		this.events = events;
	}

	public String getPageCount()
	{
		return pageCount;
	}

	public String getPage()
	{
		return page;
	}

	public List<EventEventFulAPI> getEvents()
	{
		return events;
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
		ResponseEventFulAPI.cityIndex = cityIndex;
	}
}