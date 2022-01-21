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
import com.example.greetingcards.Models.User;
import com.example.greetingcards.SendCardAdapter;
import com.example.greetingcards.databinding.FragmentGalleryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetCardsFragment extends Fragment {

    private GetCardsViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    private DatabaseReference mDataBaseSendCard;
    private String SEND_CARD_KEY = "SEND_CARD";
    public List<SendCard> mainSendCardList = new ArrayList<>();
    private DatabaseReference mDataBaseCard;
    private String CARD_KEY = "CARD";
    public List<Card> mainCardList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GetCardsViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDataBaseSendCard = FirebaseDatabase.getInstance().getReference(SEND_CARD_KEY);
        getSendCardsFromDB();
        mDataBaseCard = FirebaseDatabase.getInstance().getReference(CARD_KEY);
        getCardsFromDB();
        /*final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/



        return root;
    }
    private void getSendCardsFromDB() {

        ValueEventListener VListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mainSendCardList.size()>0)
                    mainSendCardList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    SendCard sendCard = ds.getValue(SendCard.class);
                    if (sendCard != null)
                        mainSendCardList.add(sendCard);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBaseSendCard.addValueEventListener(VListener);
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
                List<SendCard> sendCards = new ArrayList<>();

                MainActivity mainActivity = (MainActivity) getActivity();
                for (SendCard c: mainSendCardList) {
                    if(c.getToID().equals(mainActivity.authLogin))
                        sendCards.add(c);
                }

                List<Card> cards = new ArrayList<>();

                for (SendCard sc: mainSendCardList) {
                    if(sc.getToID().equals(mainActivity.authLogin)){
                        for (Card c: mainCardList) {
                            if(c.getKey().equals(sc.getCardID()))
                                cards.add(c);
                        }
                    }
                }

                RecyclerView recyclerView = binding.list;
                // создаем адаптер
                SendCardAdapter adapter = new SendCardAdapter(getActivity(), cards);
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
        binding = null;
    }
}