package com.example.greetingcards.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.User;

import java.util.List;
@Dao
public interface CardDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    @Query("DELETE FROM card_table")
    void deleteAll();

    @Query("SELECT * FROM card_table ORDER BY _id ASC")
    LiveData<List<Card>> getCardsSortedByID();

    @Query("SELECT * FROM card_table")
    List<Card> getCards();
}
