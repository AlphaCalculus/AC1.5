package com.example.materialtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ChapterListActivity extends AppCompatActivity {

    private List<Chapter> chapterList = new ArrayList<Chapter>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_list);
        switch (getIntent().getExtras().getInt("part")) {
            case R.id.nav_daoshu:
                initDerivative();
                break;
            case R.id.nav_jifen:
                initIntegral();
                break;
        }
        ChapterListAdapter adapter = new ChapterListAdapter(ChapterListActivity.this, R.layout.daoshu_item, chapterList);
        ListView listView = (ListView) findViewById(R.id.chapter_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chapter chapter = chapterList.get(position);
                Toast.makeText(ChapterListActivity.this, chapter.getChapterName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initIntegral() {

        String[] a = {"积分和导数的关系",
                "积分的表示方法",
                "积分的读法",
                "积分的计算练习",
                "什么是积分常数",
                "为什么是 C",
                "什么是原函数",
                "导数和积分真的是逆运算吗",
                "积分是变化的集合",
                "从不定积分到定积分",
                "有区间范围的积分",
                "不定积分、定积分和面积",
                "dx 的宽度",
                "分割求面积的方法",
                "定积分的不同求解方法"};
        initChapter(a, 2);
    }

    private void initDerivative() {
        String[] a = {"为什么要学数学",
                "数学过敏症对策",
                "导数有什么用",
                "某一点的斜率和瞬间斜率",
                "曲线的高峰",
                "如何画曲线图",
                "如何使用导数",
                "用导数处理图像",
                "如何求斜率",
                "怎样在曲线上取两点",
                "使曲线上的两点不断接近",
                "什么是极限",
                "什么是无限接近",
                "怎样利用数学算式表示极限",
                "极值的求法和表示方法"};
        initChapter(a, 1);
    }

    private void initChapter(String[] chapterNames, int part_index) {
        Context context = getApplicationContext();
        Chapter ch = null;
        for (int i = 0; i < 15; i++) {
            ch = new Chapter(chapterNames[i], context.getResources().getIdentifier("ch" + part_index + "-" + (i + 1), "drawable", context.getPackageName()));
            chapterList.add(ch);
        }

    }

}
