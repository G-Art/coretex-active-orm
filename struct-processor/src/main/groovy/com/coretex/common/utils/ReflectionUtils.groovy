package com.coretex.common.utils

import org.reflections.Reflections

import java.lang.annotation.Annotation

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ReflectionUtils {

    static <R> R findClasses(String path, Closure<?> closure) {
        Reflections reflections = new Reflections(path)
        closure.call(reflections) as R
    }

    static <R> R findClassesAnnotatedWith(String path, Class<? extends Annotation> aClass) {
        findClasses(path) { ref -> ((Reflections) ref).getTypesAnnotatedWith(aClass) }
    }

    static <R> R findClassesAnnotatedWith(String path, Class<? extends Annotation> aClass, Closure<R> filter) {
        filter.call(findClassesAnnotatedWith(path, aClass))
    }
}
