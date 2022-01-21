package com.example.greetingcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greetingcards.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private final int REGISTRATION_ACTIVITY_REQUEST_CODE = 1;

    private DatabaseReference mDataBase;
    private String USER_KEY = "USER";
    private List<User> mainUserList = new ArrayList<>();
    EditText login, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(EditText) findViewById(R.id.login);
        password=(EditText) findViewById(R.id.password);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        getDataFromDB();
    }
    private void getDataFromDB() {

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(VListener);
    }
    public void Registration(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivityForResult(intent, REGISTRATION_ACTIVITY_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTRATION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            User user = new User();
            user.setID(mDataBase.getKey());
            user.setName(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[0]);
            user.setEmail(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[1]);
            user.setLogin(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[2]);
            user.setPassword(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[3]);

            for (User u:mainUserList) {
                if(u.getLogin().equals(user.getLogin())){
                    Toast.makeText(
                            getApplicationContext(),
                            "Логин должен быть уникальным",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }

            mDataBase.push().setValue(user);
            Toast.makeText(
                    getApplicationContext(),
                    "Регистрация прошла успешно",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Что-то пошло не так, регистрация не удалась",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void Authorization(View view) throws ExecutionException, InterruptedException {
        List<User> userList = mainUserList;
        String enteredLogin = login.getText().toString();
        String enteredPassword = password.getText().toString();
        for (User u:userList) {
            if(u.getLogin().equals(enteredLogin) && u.getPassword().equals(enteredPassword)){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("id1",u.getLogin().toString());
                startActivity(intent);
                return;
            }
        }
        Toast.makeText(
                getApplicationContext(),
                "Введён неверный логин или пароль",
                Toast.LENGTH_LONG).show();
        return;
    }
}