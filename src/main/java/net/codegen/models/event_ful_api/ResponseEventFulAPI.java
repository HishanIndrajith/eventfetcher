package net.codegen.models.event_ful_api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="search", namespace="net.codegen" )
@XmlAccessorType(XmlAccessType.NONE)
public class ResponseEventFulAPI
{
	@XmlAttribute(name="page_count")
	private Integer pageCount;
	@XmlElement(name="page_number")
	private String page;
	@XmlElement(name="events")
	private List<EventEventFulAPI> events;

	public Integer getPageCount()
	{
		return pageCount;
	}

	public void setPageCount( Integer pageCount )
	{
		this.pageCount = pageCount;
	}

	public String getPage()
	{
		return page;
	}

	public void setPage( String page )
	{
		this.page = page;
	}

	public List<EventEventFulAPI> getEvents()
	{
		return events;
	}

	public void setEvents( List<EventEventFulAPI> events )
	{
		this.events = events;
	}
}