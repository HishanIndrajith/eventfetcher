package models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventEventBriteAPI
{
	private String url;
	private String summary;
	private String name;
//	private String city;
//	private String description;
//	private Date startDate;
//	private Date endDate;

	public EventEventBriteAPI()
	{
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("name")
	private void setName(Map<String,Object> name) {
		this.name = (String)name.get("text");
		//Map<String,String> owner = (Map<String,String>)brand.get("owner");
		//this.ownerName = owner.get("name");
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary( String summary )
	{
		this.summary = summary;
	}

	public String getName()
	{
		return name;
	}

//	public String getCity()
//	{
//		return city;
//	}
//
//	public void setCity( String city )
//	{
//		this.city = city;
//	}
//
//	public String getDescription()
//	{
//		return description;
//	}
//
//	public void setDescription( String description )
//	{
//		this.description = description;
//	}
//
//	public Date getStartDate()
//	{
//		return startDate;
//	}
//
//	public void setStartDate( Date startDate )
//	{
//		this.startDate = startDate;
//	}
//
//	public Date getEndDate()
//	{
//		return endDate;
//	}
//
//	public void setEndDate( Date endDate )
//	{
//		this.endDate = endDate;
//	}
}
