package com.example.greetingcards.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.SendCard;

import java.util.List;
@Dao
public interface SendCardDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SendCard sendCard);

    @Query("DELETE FROM send_card_table")
    void deleteAll();

    @Query("SELECT * FROM send_card_table ORDER BY _id ASC")
    LiveData<List<SendCard>> getSendCardsSortedByID();

    @Query("SELECT * FROM send_card_table")
    List<SendCard> getSendCards();
}
