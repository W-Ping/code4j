package com.code4j.annotation;

import java.lang.annotation.*;

/**
 * @author :lwp
 * @date :Created in 2022-05-21
 */

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value();

    boolean pk() default false;

    boolean auto() default false;
}
