package com.reynosh.storelisting.models;


import com.google.gson.annotations.SerializedName;


public class Page{

	@SerializedName("page-num")
	private String pageNum;

	@SerializedName("page-size")
	private String pageSize;

	@SerializedName("content-items")
	private ContentItems contentItems;

	@SerializedName("total-content-items")
	private String totalContentItems;

	@SerializedName("title")
	private String title;

	public void setPageNum(String pageNum){
		this.pageNum = pageNum;
	}

	public String getPageNum(){
		return pageNum;
	}

	public void setPageSize(String pageSize){
		this.pageSize = pageSize;
	}

	public String getPageSize(){
		return pageSize;
	}

	public void setContentItems(ContentItems contentItems){
		this.contentItems = contentItems;
	}

	public ContentItems getContentItems(){
		return contentItems;
	}

	public void setTotalContentItems(String totalContentItems){
		this.totalContentItems = totalContentItems;
	}

	public String getTotalContentItems(){
		return totalContentItems;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"Page{" + 
			"page-num = '" + pageNum + '\'' + 
			",page-size = '" + pageSize + '\'' + 
			",content-items = '" + contentItems + '\'' + 
			",total-content-items = '" + totalContentItems + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}