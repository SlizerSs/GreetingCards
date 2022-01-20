package com.example.greetingcards;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greetingcards.Models.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegistrationActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.userlistsql.REPLY";

    EditText login, password, email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
    }

    public void Register(View view) {
        if (login.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("") || name.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Intent replyIntent = new Intent();
                String[] arr = new String[]{
                        name.getText().toString(),
                        email.getText().toString(),
                        login.getText().toString(),
                        password.getText().toString()
                };
                replyIntent.putExtra(EXTRA_REPLY, arr);
                setResult(RESULT_OK, replyIntent);
                finish();
            } catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Не получилось зарегистрироваться", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init(){
        login=(EditText) findViewById(R.id.login);
        name=(EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
    }
}