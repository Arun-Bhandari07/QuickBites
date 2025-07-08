package com.QuickBites.app.annotations;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyMultipartValidator implements ConstraintValidator<NotEmptyMultipart,MultipartFile> {

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		return file!=null && !file.isEmpty();
	}
	
}
