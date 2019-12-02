package jmapps.hadith40.data.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.hadith40.presentation.ui.contents.ModelContent

class DatabaseContent(private val context: Context?) {

    private lateinit var database: SQLiteDatabase

    val getContentList: MutableList<ModelContent>
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_chapters",
                null,
                null,
                null,
                null,
                null,
                null
            )

            val contentList = ArrayList<ModelContent>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val contents = ModelContent(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("ContentArabic")),
                        cursor.getString(cursor.getColumnIndex("ContentTranslation"))
                    )
                    contentList.add(contents)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return contentList
        }
}