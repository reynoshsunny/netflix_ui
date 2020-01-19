package com.reynosh.storelisting.models;


import com.google.gson.annotations.SerializedName;


public class ContentItem{

	@SerializedName("name")
	private String name;

	@SerializedName("poster-image")
	private String posterImage;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPosterImage(String posterImage){
		this.posterImage = posterImage;
	}

	public String getPosterImage(){
		return posterImage;
	}

	@Override
 	public String toString(){
		return 
			"ContentItem{" + 
			"name = '" + name + '\'' + 
			",poster-image = '" + posterImage + '\'' + 
			"}";
		}
}