package com.coretex.build.data.items.traits

trait Insertable {

    private UUID uuid = UUID.randomUUID()

    UUID getUuid() {
        return this.uuid
    }

    void setUuid(UUID uuid) {
        this.uuid = uuid
    }


}