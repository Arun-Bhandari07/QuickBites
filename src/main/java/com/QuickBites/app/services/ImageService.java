package com.QuickBites.app.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.Exception.FileUploadException;
import com.QuickBites.app.Exception.InvalidFileException;
import com.QuickBites.app.enums.ImageType;

@Service
public class ImageService {
	@Autowired
	private CloudinaryService cloudinaryService;
	
	private static Logger logger = LoggerFactory.getLogger(ImageService.class);
	
	@Value("${file.storage.imagePath}")
	private String imagePath;
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxSize;

	public String saveImage(MultipartFile image, ImageType imageType){
		String fileName = validateImage(image);
		return cloudinaryService.uploadImage(image, imageType);
	}
	
	
	public void deleteImage(String imageName,ImageType imageType) {
		if(imageName == null || imageName.isEmpty()) {
			throw new InvalidFileException("Image is not present with name: "+imageName);
		}
		cloudinaryService.deleteImage(imageName, imageType);
	}

	public String validateImage(MultipartFile image) {
		if(image==null||image.isEmpty()) {
			logger.warn("File is null or empty to save ");
			throw new InvalidFileException("Image must not be empty for saving");
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

}
