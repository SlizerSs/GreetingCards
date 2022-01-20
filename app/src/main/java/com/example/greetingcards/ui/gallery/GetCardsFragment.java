package com.example.greetingcards.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greetingcards.MainActivity;
import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.SendCard;
import com.example.greetingcards.SendCardAdapter;
import com.example.greetingcards.ViewModels.CardViewModel;
import com.example.greetingcards.ViewModels.SendCardViewModel;
import com.example.greetingcards.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetCardsFragment extends Fragment {

    private GetCardsViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GetCardsViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        SendCardViewModel msendCardViewModel = new ViewModelProvider(getActivity()).get(SendCardViewModel.class);
        List<SendCard> sendCards = new ArrayList<>();
        List<SendCard> allSendCards = new ArrayList<>();
        try {
            allSendCards = msendCardViewModel.allSendCards();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        for (SendCard c: allSendCards) {
            if(c.getToID().equals(mainActivity.id))
                sendCards.add(c);
        }

        CardViewModel mCardViewModel = new ViewModelProvider(getActivity()).get(CardViewModel.class);
        List<Card> allCards = new ArrayList<>();
        List<Card> cards = new ArrayList<>();
        try {
            allCards = mCardViewModel.allCards();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (SendCard sc: allSendCards) {
            if(sc.getToID().equals(mainActivity.id)){
                for (Card c: allCards) {
                    if(c.getID().toString().equals(sc.getCardID().toString()))
                        cards.add(c);
                }
            }
        }

        RecyclerView recyclerView = binding.list;
        // создаем адаптер
        SendCardAdapter adapter = new SendCardAdapter(getActivity(), cards);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}