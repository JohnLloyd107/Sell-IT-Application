package com.group5.sellit.model;

public class CategoryModel {

    String categoryname, image;

    public CategoryModel() {
    }

    public CategoryModel(String categoryname, String image) {
        this.categoryname = categoryname;
        this.image = image;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
