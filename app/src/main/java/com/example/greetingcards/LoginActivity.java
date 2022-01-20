package com.example.greetingcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greetingcards.Models.User;
import com.example.greetingcards.ViewModels.UserViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private final int REGISTRATION_ACTIVITY_REQUEST_CODE = 1;
    UserViewModel mUserViewModel;
    EditText login, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(EditText) findViewById(R.id.login);
        password=(EditText) findViewById(R.id.password);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    public void Registration(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivityForResult(intent, REGISTRATION_ACTIVITY_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTRATION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            User user = new User();
            user.setName(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[0]);
            user.setEmail(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[1]);
            user.setLogin(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[2]);
            user.setPassword(data.getStringArrayExtra(RegistrationActivity.EXTRA_REPLY)[3]);

            mUserViewModel.insert(user);
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
        List<User> userList = mUserViewModel.allUsers();
        String enteredLogin = login.getText().toString();
        String enteredPassword = password.getText().toString();
        for (User u:userList) {
            if(u.getLogin().equals(enteredLogin) && u.getPassword().equals(enteredPassword)){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("id1",u.getID().toString());
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