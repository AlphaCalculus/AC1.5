package io.github.alphacalculus.alphacalculus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView itemImageView = (ImageView) findViewById(R.id.fruit_image_view);
        TextView itemContentText = (TextView) findViewById(R.id.chapter_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initChapter();
        final Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ItemActivity self = ItemActivity.this;
                self.chapterItem = self.chapterItem.getPreviousChapter();
                self.initChapter();
            }
        });
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ItemActivity self = ItemActivity.this;
                self.chapterItem = self.chapterItem.getNextChapter();
                self.initChapter();
            }
        });
        deal_with_btn_learn();
    }

    private void initChapter () {
        String itemName = chapterItem.getName();
        int itemImageId = chapterItem.getImageId();
        ImageView itemImageView = (ImageView) findViewById(R.id.fruit_image_view);
        TextView itemContentText = (TextView) findViewById(R.id.chapter_content_text);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(itemName);
        Glide.with(this).load(itemImageId).into(itemImageView);
        String fruitContent = generateItemContent(itemName);
        itemContentText.setText(fruitContent);
        deal_with_btn_learn();
    }

    private void deal_with_btn_learn() {
        final Button btn_learned = (Button) findViewById(R.id.btn_learned);
        if (chapterItem.getPartIdx()==0) {
            if (chapterItem.getChapterIdx()<=1) {
                btn_learned.setVisibility(View.VISIBLE);
                btn_learned.setText("开始学习");
                btn_learned.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ItemActivity.this, ItemActivity.class);
                        intent.putExtra(ItemActivity.CHAPTER_ITEM,
                                ChapterItemFactory
                                        .getInstance()
                                        .getChapterCached(chapterItem.getChapterIdx()+1,0));
                        startActivity(intent);
                    }
                });
            } else {
                btn_learned.setVisibility(View.INVISIBLE);
            }
        } else {
            btn_learned.setVisibility(View.VISIBLE);
            btn_learned.setText("学习完毕");
            btn_learned.setEnabled(!ChapterItemFactory.getInstance().getLearningLog().isLearned(chapterItem));
            btn_learned.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemActivity self = ItemActivity.this;
                    int part = self.chapterItem.getPartIdx();
                    int chapter = self.chapterItem.getChapterIdx();
                    ChapterItemFactory.getInstance().getLearningLog().doLearn(part, chapter);
                    btn_learned.setEnabled(false);
                }
            });
            ChapterItemFactory.getInstance().getLearningLog().register(new LearningLog.OnLearnedListener() {
                @Override
                public void onLearned(int part, int chapter) {
                    if (chapterItem.getChapterIdx()==chapter && chapterItem.getPartIdx()==part) {
                        btn_learned.setEnabled(false);
                    }
                }
            });
        }
    }
    private String generateItemContent(String itemName) {
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
