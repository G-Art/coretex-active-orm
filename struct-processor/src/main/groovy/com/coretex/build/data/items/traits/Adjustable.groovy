package com.coretex.build.data.items.traits

trait Adjustable {

    private String enhance;


    void setEnhance(String enhance){
        this.enhance = enhance
    }

    String getEnhance(){
        return this.enhance;
    }
}