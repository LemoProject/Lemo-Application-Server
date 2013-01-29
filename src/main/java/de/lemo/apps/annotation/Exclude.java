package de.lemo.apps.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {

	//Das ist eine TESTÄNDERUNG kann gelöscht werden
public String mode() default "ALL";
public String[] stylesheet();
}
