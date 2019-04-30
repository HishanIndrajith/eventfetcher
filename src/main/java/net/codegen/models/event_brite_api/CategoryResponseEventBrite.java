package net.codegen.models.event_brite_api;

import java.util.List;

public class CategoryResponseEventBrite
{
	private List<CategoryEventBrite> categories;

	public List<CategoryEventBrite> getCategories()
	{
		return categories;
	}

	public void setCategories( List<CategoryEventBrite> categories )
	{
		this.categories = categories;
	}
}
