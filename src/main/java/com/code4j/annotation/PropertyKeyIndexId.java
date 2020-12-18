package com.code4j.annotation;

import java.lang.annotation.*;

/**
 * @author liu_wp
 * @date Created in 2020/12/18 9:48
 * @see
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyKeyIndexId {
    /**
     * @return
     */
    String fieldName() default "index";
}
