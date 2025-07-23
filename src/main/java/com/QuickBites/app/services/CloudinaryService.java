package com.QuickBites.app.services;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.Exception.FileHandlingException;
import com.QuickBites.app.Exception.InvalidFileException;
import com.QuickBites.app.configurations.CloudinaryConfig;
import com.QuickBites.app.enums.ImageType;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
	
	@Value("${cloudinary.images.base_url}")
	private String baseUrl;
	
	public static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

	private final CloudinaryConfig config;
	
	public CloudinaryService(CloudinaryConfig config) {
		this.config=config;
	}
	
	public String uploadImage(MultipartFile image,ImageType imageType) {
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("folder", resolveFolder(imageType));
			map.put("resource_type", "image");
			Map uploadResult = config.cloudinary().uploader().upload(image.getBytes(),map);
			return (String) uploadResult.get("secure_url");
		}catch(IOException ex){
			logger.error("Unable to upload image to remote Service: "+image.getOriginalFilename());
			throw new RuntimeException("Unable to upload image:"+image.getOriginalFilename()+ex);
		}
		
	}
	
	public void deleteImage(String imageUrl , ImageType imageType) {
		if(imageUrl==null || imageUrl.isEmpty()) {
			throw new InvalidFileException("Cannot delete empty or null file");
		}
		try {
			if(!imageUrl.startsWith(baseUrl)) {
				throw new IllegalArgumentException("Cannot delete file from cloudinary due to invalid prefix of image Url");
			}
				
			String pathWithVersion= imageUrl.replace(baseUrl, "");
			String pathWithoutVersion = pathWithVersion.substring((pathWithVersion.indexOf("/")+1));
			String pathWithoutExtension = pathWithoutVersion.substring(0,pathWithoutVersion.lastIndexOf("."));
			
			Map result = config.cloudinary().uploader().destroy(pathWithoutExtension, Collections.EMPTY_MAP);
			String status =(String) result.get("result");
			if(!"ok".equals(status)) {
			     logger.warn("Cloudinary deletion did not return 'ok': {}", result);
	        } else {
	            logger.info("Image deleted from Cloudinary: {}", pathWithoutExtension);
	        }
		}catch(IOException ex) {
		throw new FileHandlingException("Cannot delete file from cloudinary",ex.getCause());
		}
		
	}
	

	public String moveAgentFile(String imageUrl) {
		try {
         if (!imageUrl.startsWith(baseUrl)) {
             throw new IllegalArgumentException("Invalid Cloudinary URL");
         }

         String pathWithVersion = imageUrl.replace(baseUrl, ""); //v1234/uploads/images/...
         String pathWithoutVersion = pathWithVersion.substring((pathWithVersion.indexOf("/")+1)); // "v1234"	
         String pathWithoutExtension = pathWithoutVersion.substring(0,pathWithoutVersion.lastIndexOf(".")); // remove `.jpg`
         
         // Build target public_id (change folder)
         if (!pathWithoutExtension.startsWith("uploads/images/foodDeliveryAgentRequestImages/")) {
             throw new IllegalArgumentException("Not in request image folder.");
         }

         String filename = pathWithoutExtension.substring("uploads/images/foodDeliveryAgentRequestImages/".length());
         String newPublicId = "uploads/images/foodDeliveryAgentImages/" + filename;

         //Rename in Cloudinary
         Map result = config.cloudinary().uploader().rename(pathWithoutExtension, newPublicId, ObjectUtils.asMap("overwrite", true));

         System.out.println("Successfully moved image from '" + pathWithoutExtension + "' to '" + newPublicId + "'");
         
         //  Construct new image URL (without version, but valid)
         String secureUrl = (String) result.get("secure_url");
         if (secureUrl == null || !secureUrl.startsWith("https://")) {
             throw new RuntimeException("Cloudinary returned invalid URL for image: " + newPublicId);
         }
         return secureUrl;
		} catch (Exception e) {
	        System.err.println("Failed to move image for Cloudinary ID. Error: " + e.getMessage());
            throw new RuntimeException("Failed to move image in Cloudinary", e);
        }
		
	}
	
	
	public String  resolveFolder(ImageType imageType) {
		switch(imageType) {
		case FOOD_CATEGORY: 
			return "uploads/images/foodCategoryImages";
		case FOOD_ITEM:
			return "uploads/images/foodItemImages";
		case DELIVERYAGENT:
			return "uploads/images/foodDeliveryAgentImages";
		case DELIVERYAGENTREQUEST:
			return "uploads/images/foodDeliveryAgentRequestImages";
			default:
				throw new IllegalArgumentException("Unknow image type: "+ imageType);
		}
	}
	
}
