package com.example.arturolopez.fete;

public class Party {
    public String partyName;
    public String date;
    public String hostName;
    public String price;
    public String address;
    public String description;
    public String partyid;
    public String imageUrl;

    public Party(){}

    public Party(String partyName, String date, String hostName, String price, String address, String description, String partyid, String imageUrl) {
        this.partyName = partyName;
        this.date = date;
        this.hostName = hostName;
        this.price = price;
        this.address = address;
        this.description = description;
        this.partyid = partyid;
        this.imageUrl = imageUrl;
    }


    public String getPartyName() {
        return partyName;
    }

    public String getDate() {
        return date;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPartyid() {
        return partyid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
