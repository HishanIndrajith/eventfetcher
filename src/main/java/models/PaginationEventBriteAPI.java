package models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationEventBriteAPI
{
	private int object_count;
	private int page_number;
	private int page_size;
	private int page_count;
	private boolean has_more_items;

	public PaginationEventBriteAPI()
	{
	}

	public PaginationEventBriteAPI( int object_count, int page_number, int page_size, int page_count,
			boolean has_more_items )
	{
		this.object_count = object_count;
		this.page_number = page_number;
		this.page_size = page_size;
		this.page_count = page_count;
		this.has_more_items = has_more_items;
	}

	public int getObject_count()
	{
		return object_count;
	}

	public void setObject_count( int object_count )
	{
		this.object_count = object_count;
	}

	public int getPage_number()
	{
		return page_number;
	}

	public void setPage_number( int page_number )
	{
		this.page_number = page_number;
	}

	public int getPage_size()
	{
		return page_size;
	}

	public void setPage_size( int page_size )
	{
		this.page_size = page_size;
	}

	public int getPage_count()
	{
		return page_count;
	}

	public void setPage_count( int page_count )
	{
		this.page_count = page_count;
	}

	public boolean isHas_more_items()
	{
		return has_more_items;
	}

	public void setHas_more_items( boolean has_more_items )
	{
		this.has_more_items = has_more_items;
	}
}
