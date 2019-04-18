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

}