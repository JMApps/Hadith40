package jmapps.hadith40.presentation.mvp.chapters

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class ChaptersModel(
    private val database: SQLiteDatabase?,
    private val chapterView: ChapterContract.ChapterView?) : ChapterContract.ChapterModel {

    override fun setFavorite(state: Boolean, sectionNumber: Int) {
        try {
            val favorite = ContentValues()
            favorite.put("Favorite", state)

            database?.update(
                "Table_of_chapters",
                favorite,
                "_id = ?",
                arrayOf("$sectionNumber"))

            chapterView?.saveFavorite("key_chapter_favorite_$sectionNumber", state)
        } catch (e: Exception) {
            chapterView?.showMessage(e.toString())
        }
    }
}