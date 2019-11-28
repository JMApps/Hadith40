package jmapps.hadith40.presentation.mvp.chapters

import android.database.sqlite.SQLiteDatabase

class ChapterPresenter(_chapterView: ChapterContract.ChapterView, database: SQLiteDatabase) :
    ChapterContract.ChapterPresenter {

    private var chapterView: ChapterContract.ChapterView = _chapterView
    private var chapterModel: ChapterContract.ChapterModel = ChaptersModel(database, _chapterView)

    override fun getFavorite(state: Boolean, sectionNumber: Int) {
        chapterModel.setFavorite(state, sectionNumber)
        chapterView.saveMessage(state)
    }
}