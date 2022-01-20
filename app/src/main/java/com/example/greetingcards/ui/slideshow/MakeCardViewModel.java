package com.example.greetingcards.ui.slideshow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MakeCardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MakeCardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Bitmap> getBitmap(){
        return null;

    }
}