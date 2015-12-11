package com.example.mykola.lasttest.SQLiteDataBase.DataBaseReview;

/**
 * Created by mykola on 30.11.15.
 */
public class DataProvider {

    private String id_db;
    private String name;
    private String category;
    private String mark;
    private String image_url;
    private String location;
    private String workTime;
    private String description;



    DataProvider(String id_db, String name, String category, String mark,
                 String image_url, String location, String workTime, String description){
        this.id_db = id_db;
        this.name = name;
        this.category = category;
        this.mark = mark;
        this.image_url = image_url;
        this.location = location;
        this.workTime = workTime;
        this.description = description;
    }
    public String getId_db() {
        return id_db;
    }

    public void setId_db(String id_db) {
        this.id_db = id_db;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageURL() {
        return image_url;
    }

    public void setImageURL(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
