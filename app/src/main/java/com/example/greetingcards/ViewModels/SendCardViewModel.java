package com.example.greetingcards.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.SendCard;
import com.example.greetingcards.Room.CardRepository;
import com.example.greetingcards.Room.SendCardRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class SendCardViewModel extends AndroidViewModel {

    private SendCardRepository mRepository;

    private final LiveData<List<SendCard>> mAllSendCards;

    public SendCardViewModel (Application application) {
        super(application);
        mRepository = new SendCardRepository(application);
        mAllSendCards = mRepository.getAllSendCards();
    }
    public List<SendCard> allSendCards() throws ExecutionException, InterruptedException {
        return new SendCardViewModel.allSendCardsTask().execute().get();
    }

    private class allSendCardsTask extends AsyncTask<Void, Void, List<SendCard>> {

        @Override
        protected List<SendCard> doInBackground(Void... notes) {
            return mRepository.getSendCards();
        }
    }
    LiveData<List<SendCard>> getAllSendCards() { return mAllSendCards; }

    public void insert(SendCard sendCard) { mRepository.insert(sendCard); }
}
