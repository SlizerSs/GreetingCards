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
import com.example.greetingcards.ViewModels.CardViewModel;
import com.example.greetingcards.ViewModels.SendCardViewModel;
import com.example.greetingcards.ViewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SendCardAdapter  extends RecyclerView.Adapter<SendCardAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Card> cards;
    Context context;

    public SendCardAdapter(Context context, List<Card> cards) {
        this.cards = cards;
        this.inflater = LayoutInflater.from(context);
        context = context;

    }
    @Override
    public SendCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.send_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SendCardAdapter.ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.imageView.setImageBitmap(stringToBitmap(card.getBitmap()));

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
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.card_imageview);
        }

    }
}
