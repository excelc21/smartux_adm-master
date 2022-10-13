package com.dmi.smartux.admin.abtest.vo;

public class CategoryListVo {

	private String category_id;
	private String category_name;
	private String parent_category_id;
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getParent_category_id() {
		return parent_category_id;
	}
	public void setParent_category_id(String parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
	@Override
	public String toString() {
		return "CategoryListVo [category_id=" + category_id + ", category_name="
				+ category_name + ", parent_category_id=" + parent_category_id
				+ "]";
	}


	
	
}
