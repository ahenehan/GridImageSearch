package com.codepath.gridimagesearch;

import java.io.Serializable;

public class SearchParameters implements Serializable {
	private static final long serialVersionUID = 121276742505953813L;
	
	private String imageSize;
	private String color;
	private String imageType;
	private String site;
	
	public SearchParameters (String imageSize, String color, String imageType, String site) {
		this.imageSize = imageSize.toLowerCase();
		this.color = color.toLowerCase();
		this.imageType = imageType.toLowerCase().replace(" ", "");
		this.site = site;
	}
	
	public SearchParameters() {
		this.imageSize = "all";
		this.color = "all";
		this.imageType = "all";
		this.site = "";
	}
	
	public String getImageSize() {
		return imageSize;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getImageType() {
		return imageType;
	}
	
	public String getSite() {
		return site;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize.toLowerCase();
	}

	public void setColor(String color) {
		this.color = color.toLowerCase();
	}
	
	public void setImageType(String imageType) {
		this.imageType = imageType.toLowerCase().replace(" ", "");
	}
	
	public void setSite(String site) {
		this.site = site;
	}
}
