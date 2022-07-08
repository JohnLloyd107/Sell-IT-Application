package com.group5.sellit.model;

public class FeedModel {
    String name,date,userfeed, rate;

    public FeedModel() {
    }

    public FeedModel(String name, String date, String userfeed, String rate) {
        this.name = name;
        this.date = date;
        this.userfeed = userfeed;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserfeed() {
        return userfeed;
    }

    public void setUserfeed(String userfeed) {
        this.userfeed = userfeed;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
