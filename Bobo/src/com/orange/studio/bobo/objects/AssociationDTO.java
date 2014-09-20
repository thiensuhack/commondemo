package com.orange.studio.bobo.objects;

import java.util.ArrayList;
import java.util.List;

public class AssociationDTO {
	public List<ProductImageDTO> images;
	public List<ProductCategoryDTO> categories;

	public AssociationDTO() {
		images = new ArrayList<ProductImageDTO>();
		categories = new ArrayList<ProductCategoryDTO>();
	}
}
