package com.code4j.annotation;

import java.lang.annotation.*;

/**
 * @author liu_wp
 * @date Created in 2020/12/16 11:28
 * @see
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreReflection {
}
