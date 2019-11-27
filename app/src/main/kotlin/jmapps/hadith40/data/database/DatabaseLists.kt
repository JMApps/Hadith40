package jmapps.hadith40.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import jmapps.hadith40.presentation.ui.favorites.ModelFavorite

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

    val getFavroiteList: MutableList<ModelFavorite>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_chapters",
                null,
                "Favorite = 1",
                null,
                null,
                null,
                null)

            val favoriteList = ArrayList<ModelFavorite>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val favorites = ModelFavorite(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("NumberHadeeth")),
                        cursor.getString(cursor.getColumnIndex("TitleHadeeth")))
                    favoriteList.add(favorites)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return favoriteList
        }
}