package com.example.jobquestV1;

import android.widget.Button;

import java.lang.reflect.Array;
import java.util.List;

public class ProductsModel {

    String Title;
    String name;
    String Description;
    String Cost;
    String AdID;
    List<String> ApplicantID;
    String randomID;

    private ProductsModel(String Title, String name, String Description, String Cost, String AdID, List<String> ApplicantID, String randomID) {
        this.Title = Title;
        this.name = name;
        this.Description = Description;
        this.Cost = Cost;
        this.AdID = AdID;
        this.ApplicantID = ApplicantID;
        this.randomID = randomID;
    }

    private ProductsModel() {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getAdID() {
        return AdID;
    }

    public void setAdID(String adID) {
        AdID = adID;
    }

    public List<String> getApplicantID() {
        return ApplicantID;
    }

    public void setApplicantID(List<String> applicantID) {
        ApplicantID = applicantID;
    }

    public String getRandomID() {
        return randomID;
    }

    public void setRandomID(String randomID) {
        this.randomID = randomID;
    }
}
