package jmapps.hadith40.presentation.mvp.chapters

interface ChapterContract {

    interface ChapterModel {
        fun setFavorite(state: Boolean, sectionNumber: Int)
    }

    interface ChapterView {
        fun saveFavorite(keyFavorite: String, state: Boolean)
        fun saveMessage(state: Boolean)
        fun showMessage(message: String)
    }

    interface ChapterPresenter {
        fun getFavorite(state: Boolean, sectionNumber: Int)
    }
}