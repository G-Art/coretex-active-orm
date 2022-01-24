package com.coretex.build.data.db.builder.update


import com.coretex.build.data.db.updates.Update

interface UpdateDataBuilder<RESULT extends Update, SOURCE> {
    abstract List<RESULT> build(SOURCE source)

}