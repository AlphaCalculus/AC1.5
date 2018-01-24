package io.github.alphacalculus.alphacalculus

object QuizLogDAO {
    val db  get() = UserDbHelper.getDatabase(context = TheApp.instance!!.applicationContext)
    val uid get() = TheApp.instance!!.uid

    fun finished(quizIdx: Int): Boolean {
        return scoreOf(quizIdx) > 60.0
    }

    fun scoreOf(quizIdx: Int): Double {
        val cursor = db!!.query(table_name, arrayOf("score"), "user_id = ? AND quiz_idx = ?",
                arrayOf(uid.toString(), quizIdx.toString()),null,null,null)
        if (cursor.moveToFirst()) {
            System.err.println(cursor)
            return cursor.getDouble(cursor.getColumnIndex("score"))
        } else {
            return -1.0
        }
    }

    val table_name = "user_quiz_log"

    val SQL_CREATE_ENTRIES = "CREATE TABLE ${table_name} ("+
                "user_id INTEGER," +
                "quiz_idx INTEGER, "+
                "score REAL DEFAULT 0, "+
                "PRIMARY KEY(user_id, quiz_idx) )"
    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${table_name}"
}