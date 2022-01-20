package com.example.greetingcards.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.SendCard;
import com.example.greetingcards.Models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Card.class, SendCard.class}, version = 1, exportSchema = false)
public abstract class GreetingCardsRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CardDao cardDao();
    public abstract SendCardDao sendCardDao();

    private static volatile GreetingCardsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static GreetingCardsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GreetingCardsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GreetingCardsRoomDatabase.class, "greeting_cards_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
