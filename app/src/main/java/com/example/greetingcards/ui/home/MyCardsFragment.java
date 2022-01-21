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
import com.example.greetingcards.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyCardsFragment extends Fragment {

    private MyCardsViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private DatabaseReference mDataBaseCard;
    private String CARD_KEY = "CARD";
    public List<Card> mainCardList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(MyCardsViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDataBaseCard = FirebaseDatabase.getInstance().getReference(CARD_KEY);
        getCardsFromDB();
        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/



        return root;
    }
    private void getCardsFromDB() {

        ValueEventListener VListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mainCardList.size()>0)
                    mainCardList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Card card = ds.getValue(Card.class);
                    if (card != null)
                        mainCardList.add(card);
                }
                List<Card> cards = new ArrayList<>();

                MainActivity mainActivity = (MainActivity) getActivity();
                for (Card c: mainCardList) {
                    if(c.getCreatorID().equals(mainActivity.authLogin))
                        cards.add(c);
                }
                RecyclerView recyclerView = binding.list;
                // создаем адаптер
                CardAdapter adapter = new CardAdapter(getActivity(), cards);
                // устанавливаем для списка адаптер
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBaseCard.addValueEventListener(VListener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}