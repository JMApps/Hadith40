package jmapps.hadith40.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.hadith40.presentation.ui.chapters.ModelChapter

class DatabaseLists(private val context: Context?) {

    private lateinit var database: SQLiteDatabase

    val getChapterList: MutableList<ModelChapter>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_chapters",
                null,
                null,
                null,
                null,
                null,
                null)

            val chapterList = ArrayList<ModelChapter>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val chapters = ModelChapter(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("NumberHadeeth")),
                        cursor.getString(cursor.getColumnIndex("TitleHadeeth")))
                    chapterList.add(chapters)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return chapterList
        }
}