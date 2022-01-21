package com.example.greetingcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Card> cards;
    Context context1;

    public CardAdapter(Context context, List<Card> cards) {
        this.cards = cards;
        this.inflater = LayoutInflater.from(context);
        context1 = context;

    }
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.imageView.setImageBitmap(stringToBitmap(card.getBitmap()));
        holder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = holder.loginEditText.getText().toString();
                MainActivity activity = (MainActivity)context1;
                activity.sendCard(card,login);
            }
        });
        holder.sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)context1;
                activity.sendEmailCard(card);
            }
        });
        /*holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)context1;
                activity.deleteButton(card);
            }
        });*/
    }
    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final Button sendButton;
        final EditText loginEditText;
        final Button sendEmailButton;
        //final Button deleteButton;
        ViewHolder(View view){
            super(view);
            sendButton = view.findViewById(R.id.sendButton);
            imageView = view.findViewById(R.id.card_imageview);
            loginEditText = view.findViewById(R.id.login);
            sendEmailButton = view.findViewById(R.id.sendEmailButton);
            //deleteButton = view.findViewById(R.id.deleteButton);
        }

    }
}
