package com.reynosh.storelisting.models;


import com.google.gson.annotations.SerializedName;

public class VideoResponse{

	@SerializedName("page")
	private Page page;

	public void setPage(Page page){
		this.page = page;
	}

	public Page getPage(){
		return page;
	}

	@Override
 	public String toString(){
		return 
			"VideoResponse{" + 
			"page = '" + page + '\'' + 
			"}";
		}
}