package io.github.alphacalculus.alphacalculus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChapterListActivity extends AppCompatActivity {

    private List<ChapterItem> chapterList = new ArrayList<ChapterItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);
        switch (getIntent().getExtras().getInt("part")) {
            case R.id.nav_daoshu:
                initChapters(1);
                break;
            case R.id.nav_jifen:
                initChapters(2);
                break;
        }
        final ChapterListAdapter adapter = new ChapterListAdapter(ChapterListActivity.this, R.layout.chapter_list_item, chapterList);
        final ListView listView = (ListView) findViewById(R.id.chapter_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChapterItem chapter = chapterList.get(position);
//                Toast.makeText(ChapterListActivity.this, chapter.getName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChapterListActivity.this, ItemActivity.class);
                intent.putExtra(ItemActivity.CHAPTER_ITEM, chapter);
                startActivity(intent);
            }
        });
        ChapterItemFactory.getInstance().getLearningLog().register(new LearningLog.OnLearnedListener() {
            @Override
            public void onLearned(int part, int chapter) {
                listView.setAdapter(adapter);
            }
        });
    }

    private void initChapters(int part_index) {
        /*
        for (int i = 0; i < ChapterItemFactory.getInstance().getChapterCount(part_index); i++) {
            ChapterItem ch = ChapterItemFactory.getInstance().getChapter(part_index, i);
            if (ch != null)
                chapterList.add(ch);
        }*/
        chapterList = new ArrayList<>(Arrays.asList(ChapterItemFactory.getInstance().getChapters(part_index)));
    }

}
