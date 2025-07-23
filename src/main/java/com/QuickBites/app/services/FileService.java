package com.QuickBites.app.services;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.Exception.InvalidFileException;
import com.QuickBites.app.enums.ImageType;

@Service
public class FileService {
	
	@Autowired
	CloudinaryService cloudinaryService;

	@Value("${file.storage.orgpath}")
	private  final String orgPath;
	
	@Value("${file.storage.temppath}")
	private final String tempPath;
	
	@Value("${file.storage.maxSize}")
	private final long maxFileSize;
	
	public static final Set<String> ALLOWED_EXTENSION = Set.of("jpg","png","webp","jpeg");
	public static final Set<String> ALLOWED_MIME_TYPE = Set.of("image/jpeg", "image/png","image/jpg","image/webp");
	
	public FileService(@Value("${file.storage.orgpath}") String orgPath,
						@Value("${file.storage.temppath}") String tempPath,
						@Value("${file.storage.maxSize}") String maxFileSize){
		this.orgPath = orgPath;
		this.tempPath = tempPath;
		this.maxFileSize = Long.parseLong(maxFileSize);
	}
	
	
	public String saveFile(MultipartFile file,ImageType imageType){
		validateFile(file);
		String fileName = UUID.randomUUID()+"_"+file.getOriginalFilename();
		return cloudinaryService.uploadImage(file, imageType);
	}
		
	public String moveFile(String fileName) throws IOException{
		String newImageUrl = cloudinaryService.moveAgentFile(fileName);
		return newImageUrl;
	}
	
	public void validateFile(MultipartFile file) {
		if( file==null || file.isEmpty()) {
			throw new InvalidFileException("File is null");
		}
		String orignalFileName = file.getOriginalFilename();
		long fileSize = file.getSize();
		String fileType = file.getContentType();
		String fileExtension = orignalFileName.substring(orignalFileName.lastIndexOf(".")+1).toLowerCase();
		
		//handle file size
		if(fileSize>maxFileSize) {
			throw new InvalidFileException("File exceeds maximum size limit. Max File Size:"+maxFileSize);
		}
		
		//handle file Type
		if(!ALLOWED_EXTENSION.contains(fileExtension)) {
			throw new InvalidFileException(String.format("Invalid File extension of type %s.Allowed extension "+fileExtension,ALLOWED_EXTENSION));
		}
		
		if(!ALLOWED_MIME_TYPE.contains(fileType.toLowerCase())) {
			throw new InvalidFileException(String.format("Invalid File Type of type %s.Allowed types: %s",fileType,ALLOWED_MIME_TYPE));
		}
		
	}
	
	
}
