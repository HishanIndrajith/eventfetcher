package net.codegen.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationEventBriteAPI
{

	private int objectCount;
	private int pageNumber;
	private int pageSize;
	private int pageCount;
	private boolean hasMoreItems;

	public PaginationEventBriteAPI()
	{
		//default constructor
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("object_count")
	public void setObjectCount( int objectCount )
	{
		this.objectCount = objectCount;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("page_number")
	public void setPageNumber( int pageNumber )
	{
		this.pageNumber = pageNumber;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("page_size")
	public void setPageSize( int pageSize )
	{
		this.pageSize = pageSize;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("page_count")
	public void setPageCount( int pageCount )
	{
		this.pageCount = pageCount;
	}

	@SuppressWarnings("unchecked")
	@JsonProperty("has_more_items")
	public void setHasMoreItems( boolean hasMoreItems )
	{
		this.hasMoreItems = hasMoreItems;
	}

	public int getObjectCount()
	{
		return objectCount;
	}

	public int getPageNumber()
	{
		return pageNumber;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public boolean isHasMoreItems()
	{
		return hasMoreItems;
	}
}
