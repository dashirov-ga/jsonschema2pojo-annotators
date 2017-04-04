package com.snowplowanalytics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dashirov on 4/1/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface IgluInfo {
    String vendor() ;
    String name();
    String version();
    String format() default "jsonschema";
}
