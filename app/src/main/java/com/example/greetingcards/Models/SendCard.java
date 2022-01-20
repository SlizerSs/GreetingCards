package com.example.greetingcards.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "send_card_table")
public class SendCard {

    //private Card card;
    //private User user;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private Integer mID;

    @NonNull
    @ColumnInfo(name = "card_id")
    private String mCardID;

    @NonNull
    @ColumnInfo(name = "from_userID")
    private String mFromID;

    @NonNull
    @ColumnInfo(name = "to_userID")
    private String mToID;


    public void setID(@NonNull Integer id) {this.mID = id;}
    public Integer getID(){return this.mID;}
    public void setCardID(@NonNull String CardId) {this.mCardID = CardId;}
    public String getCardID(){return this.mCardID;}
    public void setFromID(@NonNull String fromID) {this.mFromID = fromID;}
    public String getFromID(){return this.mFromID;}
    public void setToID(@NonNull String toID) {this.mToID = toID;}
    public String getToID(){return this.mToID;}

}
