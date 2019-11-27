package jmapps.hadith40.data.database

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import jmapps.hadith40.R

private var databaseVersion = 1

class DatabaseOpenHelper(context: Context?) : SQLiteAssetHelper(
    context, context?.getString(R.string.database_name), null, databaseVersion) {

    init {
        setForcedUpgrade(databaseVersion)
    }
}