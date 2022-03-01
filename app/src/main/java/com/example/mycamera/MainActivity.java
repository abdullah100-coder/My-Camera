package com.example.mycamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    TextView capture, save;
    ImageView image;
    private String currentPhotoPath;
    int namesort = 1;
    int number;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture = findViewById(R.id.capture);
        save = findViewById(R.id.save);
        image = findViewById(R.id.image);



        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(filename+namesort,".jpg, storageDirectory");
                    currentPhotoPath = imageFile.getAbsolutePath();
                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this,"com.example.mycamera.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                   startActivityForResult(intent,1);
                   namesort++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                saveImageToGalllery(bitmap);
            }
        });
    }
    private void saveImageToGalllery(Bitmap bitmap) {
        FileOutputStream fos;
        try {
            {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

                Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            image.setImageBitmap(bitmap);
        }
    }

}