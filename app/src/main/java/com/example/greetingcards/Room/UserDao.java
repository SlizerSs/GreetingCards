package com.example.greetingcards.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.greetingcards.Models.User;

import java.util.List;

@Dao
public interface UserDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table ORDER BY login ASC")
    LiveData<List<User>> getUsersSortedByLogin();

    @Query("SELECT * FROM user_table")
    List<User> getUsers();
}
