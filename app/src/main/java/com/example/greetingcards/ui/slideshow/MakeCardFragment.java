package com.example.greetingcards.ui.slideshow;

import android.Manifest;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.greetingcards.MainActivity;
import com.example.greetingcards.Models.Card;
import com.example.greetingcards.R;
import com.example.greetingcards.databinding.FragmentSlideshowBinding;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MakeCardFragment extends Fragment implements ColorPickerDialogListener {
    final int REQUEST_CODE_GALLERY = 999;
    private FragmentSlideshowBinding binding;
    Button colorButton;
    //Button textButton;
    Button galleryButton;
    Button saveButton;
    ImageView cardImageView;
    Bitmap savedBitmap;
    EditText cardEditText;

    RangeSlider rangeSlider;
    RangeSlider rangeSlider2;
    String text = null;
    int globalX = 10;
    int globalY = 50;

    private DatabaseReference mDataBaseCard;
    private String CARD_KEY = "CARD";
    public List<Card> mainCardList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mDataBaseCard = FirebaseDatabase.getInstance().getReference(CARD_KEY);
        getCardsFromDB();
        cardEditText = binding.cardEditText;
        colorButton = binding.firstButton;
        galleryButton = binding.secondButton;
        //textButton = binding.textButton;
        saveButton = binding.saveButton;
        //textButton.setEnabled(false);
        cardImageView = binding.cardImageview;
        rangeSlider = binding.widthSlider;
        rangeSlider2 = binding.heightSlider;
        saveButton.setEnabled(false);
        /*final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                globalX = (int)value;
                setText(globalX, globalY);
            }
        });
        rangeSlider2.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                globalY = (int)value;
                setText(globalX, globalY);
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColorPickerDialog(1);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePhoto();
            }
        });
        /*textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText(10,50);
            }
        });*/
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = saveImageToGallery();

                MainActivity mainActivity = (MainActivity) getActivity();
                Card card = new Card();
                card.setCreatorID(mainActivity.authLogin);
                card.setBitmap(bitmapToString(bitmap));
                DatabaseReference myRef = mDataBaseCard.push();
                String key = myRef.getKey();
                card.setKey(key);

                mDataBaseCard.push().setValue(card);
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "Успешно сохранено",
                        Toast.LENGTH_LONG).show();
            }
        });

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBaseCard.addValueEventListener(VListener);
    }
    public final static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private void setText(int x, int y){
        text = cardEditText.getText().toString();
        Bitmap bitmap = Bitmap.createBitmap(savedBitmap);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        canvas.drawText(text, x, y, paint);
        cardImageView.setImageBitmap(bitmap);
    }
    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case 1:
                int x1 = 0;
                int y1 = 0;
                int x2 = 660;
                int y2 = 312;

                Bitmap bitmap = Bitmap.createBitmap(660, 312, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(color);
                canvas.drawRect(x1,y1,x2,y2,paint);
                savedBitmap = bitmap;
                text = null;
                //textButton.setEnabled(true);
                saveButton.setEnabled(true);
                cardImageView.setImageBitmap(bitmap);
                break;
        }
    }
    @Override
    public void onDialogDismissed(int dialogId) {
        Toast.makeText(getActivity(), "Dialog dismissed", Toast.LENGTH_SHORT).show();
    }

    public void backgroundImage(Bitmap bitmap){
        int x1 = 0;
        int y1 = 0;
        int x2 = 660;
        int y2 = 312;
        Rect size = new Rect(x1,y1,x2,y2);
        Bitmap bitmap1 = Bitmap.createBitmap(660, 312, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(bitmap, null, size, paint);
        savedBitmap = bitmap1;
        text = null;
        //textButton.setEnabled(true);
        saveButton.setEnabled(true);
        cardImageView.setImageBitmap(bitmap1);
    }
    public void ChoosePhoto(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
    }
    private Bitmap saveImageToGallery(){
        Bitmap bitmap = ((BitmapDrawable)cardImageView.getDrawable()).getBitmap();
        saveImage(bitmap);
        return bitmap;
    }
    private void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, getActivity().getApplicationContext().getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    getActivity().getApplicationContext().getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}