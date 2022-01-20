package com.example.greetingcards.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greetingcards.CardAdapter;
import com.example.greetingcards.MainActivity;
import com.example.greetingcards.Models.Card;
import com.example.greetingcards.ViewModels.CardViewModel;
import com.example.greetingcards.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyCardsFragment extends Fragment {

    private MyCardsViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(MyCardsViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        CardViewModel mCardViewModel = new ViewModelProvider(getActivity()).get(CardViewModel.class);
        List<Card> cards = new ArrayList<>();
        List<Card> allCards = new ArrayList<>();
        try {
            allCards = mCardViewModel.allCards();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        for (Card c: allCards) {
            if(c.getCreatorID().equals(mainActivity.id))
                cards.add(c);
        }
        RecyclerView recyclerView = binding.list;
        // создаем адаптер
        CardAdapter adapter = new CardAdapter(getActivity(), cards);
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