package models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventEventBriteAPI
{
	private String url;
	private String summary;

	public EventEventBriteAPI()
	{
	}

	public EventEventBriteAPI( String url, String summary )
	{
		this.url = url;
		this.summary = summary;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("brand")
	private void unpackNested(Map<String,Object> brand) {
		this.brandName = (String)brand.get("name");
		Map<String,String> owner = (Map<String,String>)brand.get("owner");
		this.ownerName = owner.get("name");
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
}
