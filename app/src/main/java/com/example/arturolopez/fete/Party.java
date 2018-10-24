package com.example.arturolopez.fete;

public class Party {
    public String partyName;
    public String hostName;
    public String price;
    public String address;
    public String description;
    public String partyid;

    public Party(){}

    public Party(String partyName, String hostName, String price, String address, String description, String partyid) {
        this.partyName = partyName;
        this.hostName = hostName;
        this.price = price;
        this.address = address;
        this.description = description;
        this.partyid = partyid;
    }


    public String getPartyName() {
        return partyName;
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

}
