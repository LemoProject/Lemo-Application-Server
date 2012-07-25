package de.lemo.apps.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {

public String mode() default "ALL";
public String[] stylesheet();
}
