package com.coretex.common.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.FOR_ALL

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface EssentialDataItemColumn {

    EssentialDataItem.EssentialItem itemCode() default FOR_ALL

    Class<?> supportedItem() default Void

    String columnName()

}