package io.github.alphacalculus.alphacalculus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

class ItemActivity : AppCompatActivity() {

    private var chapterItem: ChapterItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        val intent = intent
        chapterItem = intent.extras!!.getParcelable<ChapterItem>(CHAPTER_ITEM)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        initChapter()
        val btn_back = findViewById(R.id.btn_back) as Button
        btn_back.setOnClickListener(fun (_) {
            val self = this@ItemActivity
            self.chapterItem = self.chapterItem!!.previousChapter
            self.initChapter()
        })
        val btn_next = findViewById(R.id.btn_next) as Button
        btn_next.setOnClickListener(fun (_) {
            val self = this@ItemActivity
            self.chapterItem = self.chapterItem!!.nextChapter
            self.initChapter()
        })
        deal_with_btn_learn()
    }

    override fun onStart() {
        super.onStart()
        deal_with_btn_learn()
    }

    private fun initChapter() {
        val itemName = chapterItem!!.name
        val itemImageId = chapterItem!!.imageId
        val itemImageView = findViewById(R.id.fruit_image_view) as ImageView
        val itemContentText = findViewById(R.id.chapter_content_text) as TextView
        val collapsingToolbar = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.setTitle(itemName)
        Glide.with(this).load(itemImageId).into(itemImageView)
        val fruitContent = generateItemContent(itemName)
        itemContentText.text = fruitContent
        deal_with_btn_learn()
    }

    private fun deal_with_btn_learn() {
        val btn_learned = findViewById(R.id.btn_learned) as Button
        btn_learned.visibility = View.VISIBLE
        if (chapterItem!!.partIdx == 0) {
            if (chapterItem!!.chapterIdx <= 1) {
                btn_learned.text = "开始学习"
                btn_learned.setOnClickListener {
                    val intent = Intent(this@ItemActivity, ItemActivity::class.java)
                    intent.putExtra(ItemActivity.CHAPTER_ITEM,
                            ChapterItemFactory
                                    .getChapterCached(chapterItem!!.chapterIdx + 1, 0))
                    startActivity(intent)
                }
            } else {
                btn_learned.visibility = View.INVISIBLE
            }
        } else if (chapterItem!!.video != Uri.EMPTY) {
            btn_learned.text = "开始学习"
            btn_learned.isEnabled = true
            btn_learned.setOnClickListener {
                val intent = Intent(this@ItemActivity, VideoPlayActivity::class.java)
                intent.putExtra(ItemActivity.CHAPTER_ITEM,
                        ChapterItemFactory
                                .getChapterCached(chapterItem!!.partIdx, chapterItem!!.chapterIdx))
                startActivity(intent)
            }
        } else {
            btn_learned.text = "学习完毕"
            val learningLogDAO = LearningLogDAO(applicationContext)
            btn_learned.isEnabled = !learningLogDAO.isLearned(chapterItem!!)
            btn_learned.setOnClickListener(fun(_) {
                learningLogDAO.setLearned(chapterItem!!)
                btn_learned.isEnabled = false
            })
        }
    }

    private fun generateItemContent(itemName: String): String {
        return chapterItem!!.content
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (chapterItem!!.partIdx > 0) {
            val intent = Intent(this, ChapterListActivity::class.java)
            intent.putExtra("part", chapterItem!!.partIdx)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        val ITEM_NAME = "item_name"

        val ITEM_IMAGE_ID = "item_image_id"

        val CHAPTER_ITEM = "chapter_item"
    }
}
