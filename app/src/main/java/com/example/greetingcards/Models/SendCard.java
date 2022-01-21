package com.example.greetingcards.Models;

public class SendCard {

    private String mID;
    private String mCardID;
    private String mFromID;
    private String mToID;

    public void setID(String id) {this.mID = id;}
    public String getID(){return this.mID;}
    public void setCardID(String CardId) {this.mCardID = CardId;}
    public String getCardID(){return this.mCardID;}
    public void setFromID(String fromID) {this.mFromID = fromID;}
    public String getFromID(){return this.mFromID;}
    public void setToID(String toID) {this.mToID = toID;}
    public String getToID(){return this.mToID;}

}
