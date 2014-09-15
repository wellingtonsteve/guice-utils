package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

/**
 * Annotation used on classes that should be destroyed once the component
 * they were required for is destroyed. 
 * 
 * IMPORTANT: Any components annotated with DestroyableComponentSingleton
 * must have a method annotated by @PreDestroy, otherwise they will not
 * be destroyed by the Destroyer class
 * 
 * @author ckurzeja
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface DestroyableComponentSingleton {}