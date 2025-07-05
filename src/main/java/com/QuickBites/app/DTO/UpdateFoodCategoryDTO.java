package com.QuickBites.app.DTO;

import org.springframework.web.multipart.MultipartFile;

public class UpdateFoodCategoryDTO {

	private String name;
	private String description;
	private MultipartFile image;
	private Boolean active;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public MultipartFile getImage () {
		return image;
	}
	public void setImage(MultipartFile image ) {
		this.image  = image ;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
