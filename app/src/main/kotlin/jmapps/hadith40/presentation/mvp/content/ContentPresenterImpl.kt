package jmapps.hadith40.presentation.mvp.content

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class ContentPresenterImpl(
    private val contentView: ContentContract.ContentView?,
    private val sectionNumber: Int?,
    private val database: SQLiteDatabase?) :
    ContentContract.ContentPresenter {

    private var hadeethArabic: String? = null
    private var hadeethTranslation: String? = null

    override fun initDatabaseContent() {
        try {
            val mainCursor: Cursor = database!!.query(
                "Table_of_chapters",
                arrayOf("ContentArabic", "ContentTranslation"),
                "_id = ?",
                arrayOf("$sectionNumber"),
                null, null, null)

            if (!mainCursor.isClosed && mainCursor.moveToFirst()) {

                hadeethArabic = mainCursor.getString(0)
                hadeethTranslation = mainCursor.getString(1)

                contentView?.initDatabaseContent(hadeethArabic!!, hadeethTranslation!!)
            }

            if (mainCursor.isClosed) {
                mainCursor.close()
            }

        } catch (e: Exception) {
            contentView?.databaseException(e)
        }
    }

    override fun shareContent() {
        contentView?.shareContent(hadeethArabic!!, hadeethTranslation!!)
    }
}