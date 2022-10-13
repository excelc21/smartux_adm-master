package com.dmi.smartux.admin.keyword.vo;

import java.io.Serializable;

public class SearchKeywordResultVo implements Serializable {

	private static final long serialVersionUID = 1435580578014181043L;
	
	public SearchKeywordResultVo() {}
	
	// 현재 노드의 ID
	private String id;
	
	// 부모 노드의 ID
	private String parent;
	
	// 화면에 표시될 문자열
	private String text;
	
	// 확장된 상태로 표시할지에 대한 여부
	private boolean expanded = false;
	
	// 자식 노드가 존재하는지에 대한 여부
	private boolean hasChildren;
	
	// HTML class 속성값
	private String classes = "class";

	// 타입
	private String type;
	
	// child 데이터가 없어도 오픈아이콘을 넣을 건지
	private boolean compulsoryIcon = true;

	// 사용여부
	private boolean use_yn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCompulsoryIcon() {
		return "D".equals(type) ? compulsoryIcon : false;
	}

	public void setCompulsoryIcon(boolean compulsoryIcon) {
		this.compulsoryIcon = compulsoryIcon;
	}

	public boolean isUse_yn() {
		return use_yn;
	}

	public void setUse_yn(boolean use_yn) {
		this.use_yn = use_yn;
	}
	
}
