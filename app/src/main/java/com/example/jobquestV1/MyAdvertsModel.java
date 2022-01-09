package com.example.jobquestV1;

import java.util.List;
//Model class with setters and getter to retrieve relevant data
public class MyAdvertsModel {

    String Title;
    String Description;
    String randomID;

    MyAdvertsModel(String Title, String Description, String randomID) {

        this.Title = Title;
        this.Description = Description;
        this.randomID = randomID;

    }

    MyAdvertsModel() {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRandomID() {
        return randomID;
    }

    public void setRandomID(String randomID) {
        this.randomID = randomID;
    }
}
