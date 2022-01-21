package com.example.greetingcards.Models;

public class Card {

    private String mID;
    private String mKey;
    private String mCreatorID;
    private String mBitmap;


    public Card() {

    }

    public void setID(String id) {this.mID = id;}
    public String getID(){return this.mID;}
    public void setKey(String key) {this.mKey = key;}
    public String getKey(){return this.mKey;}
    public void setCreatorID(String creatorID) {this.mCreatorID = creatorID;}
    public String getCreatorID(){return this.mCreatorID;}
    public void setBitmap(String bitmap) {this.mBitmap = bitmap;}
    public String getBitmap(){return this.mBitmap;}
}
