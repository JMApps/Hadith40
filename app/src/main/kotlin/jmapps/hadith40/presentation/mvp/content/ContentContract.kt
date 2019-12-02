package jmapps.hadith40.presentation.mvp.content

import java.lang.Exception

interface ContentContract {

    interface ContentView {
        fun initDatabaseContent(hadeethArabic: String, hadeethTranslation: String)
        fun databaseException(e: Exception)
    }

    interface ContentPresenter {
        fun initDatabaseContent()
    }
}