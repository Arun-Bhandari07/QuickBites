package com.QuickBites.app.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.Exception.FileHandlingException;
import com.QuickBites.app.Exception.FileUploadException;
import com.QuickBites.app.Exception.InvalidFileException;
import com.QuickBites.app.enums.ImageType;

@Service
public class ImageService {
	private static Logger logger = LoggerFactory.getLogger(ImageService.class);
	
	@Value("${file.storage.imagePath}")
	private String imagePath;
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxSize;

	public String saveImage(MultipartFile image, ImageType imageType){
		String fileName = validateImage(image);
		Path dirPath = pathResolver(image,imageType);
		Path targetFilePath = dirPath.resolve(fileName).toAbsolutePath().normalize();
		try {
			Files.createDirectories(dirPath);
			Files.copy(image.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException ex) {
			throw new FileUploadException("Error on saving file"+ex.getMessage());
		}
		return fileName;
	}
	
	
	public String validateImage(MultipartFile image) {
		if(image==null||image.isEmpty()) {
			logger.warn("File is null or empty");
			throw new InvalidFileException("Image must not be empty");
		}
		String originalFileName = StringUtils.cleanPath(image.getOriginalFilename());
		String fileName = UUID.randomUUID()+"_"+System.currentTimeMillis()+"_"+originalFileName;
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
		Long fileSize =image.getSize();
		
		if(fileSize>maxSize) {
			throw new FileUploadException("Image exceeds max size");
		}
		
		if(!FileService.ALLOWED_EXTENSION.contains(fileExtension.toLowerCase())) {
			throw new FileUploadException("Image extension not allowed");
		}
		
		if(!FileService.ALLOWED_MIME_TYPE.contains(image.getContentType().toLowerCase())) {
			throw new FileUploadException("The image type is not allowed ");
		}
		return fileName;
	}
	
	public Path pathResolver(MultipartFile image, ImageType imageType) {
		Path basePath = Paths.get(imagePath).toAbsolutePath().normalize();
		
		if(imageType.equals(ImageType.FOOD_CATEGORY)) {
			basePath= basePath.resolve("FoodCategoryImages").toAbsolutePath().normalize();
			return basePath;
		}else if(imageType.equals(ImageType.FOOD_ITEM)) {
			basePath = basePath.resolve("FoodItemImages").toAbsolutePath().normalize();
			return basePath;
		}else {
			throw new FileUploadException("Error with file path");
		}
	}
	
	public void deleteImage(String imageName,ImageType imageType) {
		if(imageName == null || imageName.isEmpty()) {
			throw new InvalidFileException("Image is not present with name: "+imageName);
		}
		Path path = Paths.get(imagePath);
		if(imageType.equals(ImageType.FOOD_CATEGORY)) {
			path = path.resolve("FoodCategoryImages");
		}else if(imageType.equals(ImageType.FOOD_ITEM)) {
			path = path.resolve("FoodItemImages");
		}else if(imageType.equals(ImageType.DELIVERYAGENT)) {
			path= path.resolve("deliveryAgentImages");
		}else if(imageType.equals(ImageType.DELIVERYAGENTREQUEST)) {
			path = path.resolve("deliveryAgentRequestImages");
		}
		path = path.resolve(imageName).normalize().toAbsolutePath();
		try {
			Files.deleteIfExists(path);
		} catch (IOException ex) {
			throw new FileHandlingException("Error on deleting image:"+imageName+" "+ex.getMessage(),ex);
		}
	}
	
	
}
