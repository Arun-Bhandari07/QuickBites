package com.QuickBites.app.DTO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCategoryDTO {
	
	@NotBlank(message="Name should be provided")
	@NotNull(message="Name should not be null")
	private String name;
	
	@NotBlank(message="Description must be provided")
	@NotNull(message="Proper Description must be given")
	private String description;
	
	@NotNull(message="Please provide the representation image for Category")
	private MultipartFile image;
	
	@NotNull(message="Active status of food must be provided")
	private Boolean isActive;

	
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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}



	
}
