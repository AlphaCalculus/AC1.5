
package io.github.alphacalculus.alphacalculus

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

class LearningLogDAO(context: Context) {


    private val db: SQLiteDatabase

    init {
        db = UserDbHelper.getDatabase(context)!!
    }

    private val uid get() = TheApp.instance!!.uid

    fun setLogPoint(chapterItem: ChapterItem,msec:Int) {
        val cv = ContentValues().apply {
            put("user_id", uid)
            put("part_idx", chapterItem.partIdx)
            put("chapter_idx", chapterItem.chapterIdx)
            put("watched", msec)
        }
        insertOrUpdate(chapterItem.partIdx, chapterItem.chapterIdx, cv)
    }

    fun isLearned(partIdx: Int, chapterIdx: Int): Boolean {
        val cursor = db.query(table_name,
                arrayOf("learned"),
                "user_id = ? AND part_idx = ? AND chapter_idx = ?",
                arrayOf(uid.toString(),
                        partIdx.toString(), chapterIdx.toString()),
                null,
                null,
                null
        )
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("learned"))==1
        } else {
            return false
        }
    }

    fun setLearned(chapterItem: ChapterItem) = setLearned(chapterItem.partIdx, chapterItem.chapterIdx)
    fun setLearned(partIdx: Int, chapterIdx: Int) {
        val cv = ContentValues().apply{
            put("user_id", uid)
            put("part_idx", partIdx)
            put("chapter_idx", chapterIdx)
            put("learned", 1)
        }
        insertOrUpdate(partIdx, chapterIdx,cv)
    }

    private fun insertOrUpdate(partIdx: Int, chapterIdx: Int, cv: ContentValues) {
        val whereClause = "user_id = ? AND part_idx = ? AND chapter_idx = ?"
        val whereArgs = arrayOf(uid.toString(), partIdx.toString(), chapterIdx.toString())
        if (db.update(table_name, cv, whereClause, whereArgs)==0) {
            db.insert(table_name, null, cv)
        }
    }

    fun isLearned(chi:ChapterItem): Boolean = isLearned(chi.partIdx, chi.chapterIdx)

    fun getLogPoint(chapterItem: ChapterItem): Int = getLogPoint(chapterItem.partIdx, chapterItem.chapterIdx)
    fun getLogPoint(partIdx: Int, chapterIdx: Int): Int {
        val cursor = db.query(table_name,
                arrayOf("watched"),
                "user_id = ? AND part_idx = ? AND chapter_idx = ?",
                arrayOf(uid.toString(),
                        partIdx.toString(), chapterIdx.toString()),
                null,
                null,
                null
        )
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("watched"))
        } else {
            return 0
        }
    }

    companion object {
        val table_name = "user_learning_log"

        val SQL_CREATE_ENTRIES = "CREATE TABLE ${table_name} ("+
                "user_id INTEGER," +
                "part_idx INTEGER, "+
                "chapter_idx INTEGER, "+
                "learned INTEGER DEFAULT 0, "+
                "watched INTEGER DEFAULT 0," +
                "PRIMARY KEY(user_id, part_idx, chapter_idx) )"

        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${table_name}"
    }
}
