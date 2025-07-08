package com.QuickBites.app.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NotEmptyMultipartValidator.class)
public @interface NotEmptyMultipart {
	
	String message() default "File must not be empty";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
}
