package com.example.greetingcards;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greetingcards.Models.Card;
import com.example.greetingcards.Models.SendCard;
import com.example.greetingcards.Models.User;
import com.example.greetingcards.ui.slideshow.MakeCardFragment;
import com.google.android.material.navigation.NavigationView;


import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greetingcards.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener {
    final int REQUEST_CODE_GALLERY = 999;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public String authLogin;

    private DatabaseReference mDataBaseUser;
    private String USER_KEY = "USER";
    public List<User> mainUserList = new ArrayList<>();
    private DatabaseReference mDataBaseSendCard;
    private String SEND_CARD_KEY = "SEND_CARD";
    public List<SendCard> mainSendCardList = new ArrayList<>();

    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            authLogin = null;
        } else {
            authLogin = extras.getString("id1");
        }



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDataBaseUser = FirebaseDatabase.getInstance().getReference(USER_KEY);
        getUsersFromDB();
        mDataBaseSendCard = FirebaseDatabase.getInstance().getReference(SEND_CARD_KEY);
        getSendCardsFromDB();

        setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }
    private void getUsersFromDB() {

        ValueEventListener VListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mainUserList.size()>0)
                    mainUserList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (user != null)
                        mainUserList.add(user);
                }
                User user = new User();
                for (User u:mainUserList) {
                    if(u.getLogin().equals(authLogin)){
                        user = u;
                    }
                }

                View headerView = navigationView.getHeaderView(0);
                TextView navUsername = (TextView) headerView.findViewById(R.id.textViewUserName);
                navUsername.setText(user.getName());
                TextView navMail = (TextView) headerView.findViewById(R.id.textViewMail);
                navMail.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBaseUser.addValueEventListener(VListener);
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
    public void sendCard(Card card, String login){
        MainActivity mainActivity = this;

        for (User u:mainUserList) {
            if(u.getLogin().equals(login)){

                SendCard sendCard = new SendCard();
                sendCard.setCardID(card.getKey());
                sendCard.setFromID(card.getCreatorID());
                sendCard.setToID(u.getLogin());
                mDataBaseSendCard.push().setValue(sendCard);
                Toast.makeText(
                        this.getApplicationContext(),
                        "Отправлено",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Операции для выбранного пункта меню
        switch (item.getItemId())
        {
            case R.id.action_makeCard:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onColorSelected(int dialogId, int color) {
        // Notify fragment
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_slideshow);

        if (fragment instanceof ColorPickerDialogListener) {
            ((ColorPickerDialogListener) fragment).onColorSelected(dialogId, color);
        }
    }
    @Override
    public void onDialogDismissed(int dialogId) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(getApplicationContext(), "You don't have permisson to access file location",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_slideshow);

                ((MakeCardFragment) fragment).backgroundImage(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}