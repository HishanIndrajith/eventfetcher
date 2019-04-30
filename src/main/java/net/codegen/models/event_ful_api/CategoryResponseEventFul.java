package net.codegen.models.event_ful_api;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.NONE)
public class CategoryResponseEventFul
{
	@XmlElement(name = "category")
	private List<CategoryEventFul> categories;

	public List<CategoryEventFul> getCategories()
	{
		return categories;
	}

}