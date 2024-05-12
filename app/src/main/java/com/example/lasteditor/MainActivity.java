package com.example.lasteditor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;

    private ImageView imageView;
    private Uri originalImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // Request WRITE_EXTERNAL_STORAGE permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
        }

        findViewById(R.id.captureImageButton).setOnClickListener(v -> {
            // Start image picker
            ImagePicker.Companion.with(MainActivity.this)
                    .crop()
                    .start(REQUEST_IMAGE_PICKER);
        });

        findViewById(R.id.start).setOnClickListener(v -> {
            if (originalImageUri != null) {
                Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
                intent.putExtra("originalImageUri", originalImageUri.toString());
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.saveImageButton).setOnClickListener(v -> saveImage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now start the image picker
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permission denied, cannot save image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                // Store the selected image URI for later use
                originalImageUri = imageUri;
                imageView.setImageURI(imageUri);
            }
        }
    }

    private void saveImage() {
        if (originalImageUri == null) {
            Toast.makeText(this, "No image to save", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve the bitmap from the selected image URI
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalImageUri);

            // Save the image to the gallery
            String savedImageURL = MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    bitmap,
                    "Filtered_Image",
                    "Image filtered and saved using LastEditor"
            );

            if (savedImageURL != null) {
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}
