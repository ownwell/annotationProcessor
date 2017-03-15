package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * //todo 描述
 *
 * @author lixingyun
 * @since 2017-03-14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ContentView {
    int value();
}