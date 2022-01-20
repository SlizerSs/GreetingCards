package com.example.greetingcards.Room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.SendCard;

import java.util.List;

public class SendCardRepository {
    private SendCardDao mSendCardDao;
    private LiveData<List<SendCard>> mAllSendCards;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public SendCardRepository(Application application) {
        GreetingCardsRoomDatabase db = GreetingCardsRoomDatabase.getDatabase(application);
        mSendCardDao = db.sendCardDao();
        mAllSendCards = mSendCardDao.getSendCardsSortedByID();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<SendCard>> getAllSendCards() {
        return mAllSendCards;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(SendCard sendCard) {
        GreetingCardsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSendCardDao.insert(sendCard);
        });
    }
    public List<SendCard> getSendCards(){
        return mSendCardDao.getSendCards();
    }
}
