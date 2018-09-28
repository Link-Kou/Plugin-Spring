package com.plugin.javawidget.mybatisplugins;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * @author LK
 * @date 2018-05-05 16:08
 */
public @interface PaginatorType {

	/**
	 * FOUND_ROWS == true
	 * COUNT(*)  == false
	 * @return
	 */
	boolean value() default true;
}
