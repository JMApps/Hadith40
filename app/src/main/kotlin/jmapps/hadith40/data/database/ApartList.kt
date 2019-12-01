package jmapps.hadith40.data.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.hadith40.presentation.ui.apart.ModelApart

class ApartList(private val context: Context?) {

    private lateinit var database: SQLiteDatabase

    fun getApartList(sampleBy: Int): MutableList<ModelApart> {

        database = DatabaseOpenHelper(context).readableDatabase

        val cursor: Cursor = database.query(
            "Table_of_cut",
            null,
            "ContentPosition = $sampleBy",
            null,
            null,
            null,
            null
        )

        val apartList = ArrayList<ModelApart>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val apart = ModelApart(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("ContentArabic")),
                    cursor.getString(cursor.getColumnIndex("ContentTranslation")),
                    cursor.getString(cursor.getColumnIndex("NameAudio"))
                )
                apartList.add(apart)
                cursor.moveToNext()
                if (cursor.isClosed) {
                    cursor.close()
                }
            }
        }
        return apartList
    }
}