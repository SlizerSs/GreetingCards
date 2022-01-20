package com.example.greetingcards.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.User;
import com.example.greetingcards.Room.CardRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CardViewModel extends AndroidViewModel {

    private CardRepository mRepository;

    private final LiveData<List<Card>> mAllCards;

    public CardViewModel (Application application) {
        super(application);
        mRepository = new CardRepository(application);
        mAllCards = mRepository.getAllCards();
    }
    public List<Card> allCards() throws ExecutionException, InterruptedException {
        return new CardViewModel.allCardsTask().execute().get();
    }

    private class allCardsTask extends AsyncTask<Void, Void, List<Card>> {

        @Override
        protected List<Card> doInBackground(Void... notes) {
            return mRepository.getCards();
        }
    }
    LiveData<List<Card>> getAllCards() { return mAllCards; }

    public void insert(Card card) { mRepository.insert(card); }
}
