package io.github.alphacalculus.alphacalculus;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class ChapterListActivity extends AppCompatActivity {

    private List<Chapter> chapterList = new ArrayList<Chapter>();

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

    private void initChapters(int part_index) {
        Chapter ch = null;
        Context context = getApplicationContext();
        Resources res = context.getResources();
        InputSource inputSrc = new InputSource(getResources().openRawResource(R.raw.itemdata));
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodes = (NodeList) xpath.evaluate("//Data/Part[@index=" + part_index + "]/Chapter", inputSrc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                ch = new Chapter((String) xpath.evaluate("./@title", nodes.item(i), XPathConstants.STRING),
                        context.getResources().getIdentifier("ch" + part_index + "-" + (i + 1),
                                "drawable", context.getPackageName()));
                chapterList.add(ch);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

}
