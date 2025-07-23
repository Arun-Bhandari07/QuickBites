package com.QuickBites.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.services.CloudinaryService;

@RestController
//@RequestMapping("/api/cloudinaryImages")
public class CloudinaryController {
	
	@Autowired
	CloudinaryService cloudinaryService;

	@PostMapping(path="/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadImage(@RequestParam("image") MultipartFile image) {
		return cloudinaryService.uploadImage(image, ImageType.FOOD_CATEGORY);
	}
	
}
