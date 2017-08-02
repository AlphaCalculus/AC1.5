package io.github.alphacalculus.alphacalculus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ItemActivity extends AppCompatActivity {

    public static final String ITEM_NAME = "item_name";

    public static final String ITEM_IMAGE_ID = "item_image_id";

    public static final String CHAPTER_ITEM = "chapter_item";

    private ChapterItem chapterItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        chapterItem = (ChapterItem) intent.getExtras().getParcelable(CHAPTER_ITEM);
        String itemName = chapterItem.getName();
        int itemImageId = chapterItem.getImageId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView itemImageView = (ImageView) findViewById(R.id.fruit_image_view);
        TextView itemContentText = (TextView) findViewById(R.id.chapter_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(itemName);
        Glide.with(this).load(itemImageId).into(itemImageView);
        String fruitContent = generateItemContent(itemName);
        itemContentText.setText(fruitContent);
    }

    private String generateItemContent(String itemName) {
        /*
        StringBuilder chapterContent = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            chapterContent.append(itemName);
        }
        return chapterContent.toString();
            */
        return chapterItem.getContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
