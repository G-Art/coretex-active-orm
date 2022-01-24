package com.coretex.build.data.db.builder.remove

import com.coretex.build.data.db.removes.Remove

interface RemoveDataBuilder<RESULT extends Remove, SOURCE> {
    abstract List<RESULT> build(SOURCE source)
}