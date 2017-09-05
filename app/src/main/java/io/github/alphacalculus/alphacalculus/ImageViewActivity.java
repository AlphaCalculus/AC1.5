package io.github.alphacalculus.alphacalculus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Show image in a new Activity.
 */
public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        int resid = getIntent().getIntExtra("io.github.alphacalculus.IMAGE_SHOW_ID", 0);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(resid);
    }
}
