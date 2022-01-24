package com.coretex.build.data.db.builder.inserts

import com.coretex.build.data.db.inserts.Insert

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
interface InsertDataBuilder<RESULT extends Insert, SOURCE> {
    abstract List<RESULT> build()
    abstract List<RESULT> build(SOURCE source)
}
