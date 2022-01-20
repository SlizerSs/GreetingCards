package com.example.greetingcards.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "card_table")
public class Card {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private Integer mID;

    @NonNull
    @ColumnInfo(name = "creator_userID")
    private String mCreatorID;

    @NonNull
    @ColumnInfo(name = "bitmap")
    private String mBitmap;


    public Card() {

    }

    public void setID(@NonNull Integer id) {this.mID = id;}
    public Integer getID(){return this.mID;}
    public void setCreatorID(@NonNull String creatorID) {this.mCreatorID = creatorID;}
    public String getCreatorID(){return this.mCreatorID;}
    public void setBitmap(@NonNull String bitmap) {this.mBitmap = bitmap;}
    public String getBitmap(){return this.mBitmap;}
}
