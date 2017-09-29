package com.marckregio.firebasemakunat.model;

/**
 * Created by eCopy on 9/29/2017.
 */

public class SampleModel {

    public String title;
    public String description;

    public SampleModel(){

    }

    public SampleModel(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }
}
