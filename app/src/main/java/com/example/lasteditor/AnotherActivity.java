package com.example.lasteditor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.mukesh.image_processing.ImageProcessor;

public class AnotherActivity extends AppCompatActivity {

    private ImageView originalIV, oneIV, twoIV, threeIV, fourIV, fiveIV, sixIV, sevenIV, eightIV, nineIV, tenIV;
    private Bitmap originalBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        originalIV = findViewById(R.id.idIVOriginalImage);
        oneIV = findViewById(R.id.idIVOne);
        twoIV = findViewById(R.id.idIVTwo);
        threeIV = findViewById(R.id.idIVThree);
        fourIV = findViewById(R.id.idIVFour);
        fiveIV = findViewById(R.id.idIVFive);
        sixIV = findViewById(R.id.idIVSix);
        sevenIV = findViewById(R.id.idIVSeven);
        eightIV = findViewById(R.id.idIVEight);
        nineIV = findViewById(R.id.idIVNine);
        tenIV = findViewById(R.id.idIVTen);

        // Retrieve original image URI from intent extras
        String imageUriString = getIntent().getStringExtra("originalImageUri");
        Uri originalImageUri = Uri.parse(imageUriString);

        // Load original image
        try {
            originalBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(originalImageUri));
            originalIV.setImageBitmap(originalBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize image filters
        initializeImageFilters();
    }

    private void initializeImageFilters() {
        ImageProcessor processor = new ImageProcessor();

        try {
            // Apply filters
            oneIV.setImageBitmap(processor.tintImage(originalBitmap, 90));
            twoIV.setImageBitmap(processor.applyGaussianBlur(originalBitmap));
            threeIV.setImageBitmap(processor.createSepiaToningEffect(originalBitmap, 1, 2, 1, 5));
            fourIV.setImageBitmap(processor.applySaturationFilter(originalBitmap, 3));
            fiveIV.setImageBitmap(processor.applySnowEffect(originalBitmap));
            sixIV.setImageBitmap(processor.doGreyScale(originalBitmap));
            sevenIV.setImageBitmap(processor.engrave(originalBitmap));
            eightIV.setImageBitmap(processor.createContrast(originalBitmap, 1.5));
            nineIV.setImageBitmap(processor.createShadow(originalBitmap));
            tenIV.setImageBitmap(processor.applyFleaEffect(originalBitmap));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to apply filter", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
