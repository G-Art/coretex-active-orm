package com.coretex.build.converters

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
interface Converter<SOURCE, TARGET> {

    TARGET convert(SOURCE source)
}