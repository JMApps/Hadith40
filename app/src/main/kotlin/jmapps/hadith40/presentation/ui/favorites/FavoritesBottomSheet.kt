package jmapps.hadith40.presentation.ui.favorites

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.data.database.DatabaseOpenHelper
import jmapps.hadith40.presentation.mvp.chapters.ChapterContract
import jmapps.hadith40.presentation.mvp.chapters.ChapterPresenter
import kotlinx.android.synthetic.main.bottom_sheet_favorites.view.*

class FavoritesBottomSheet : BottomSheetDialogFragment(), AdapterFavorite.OnItemClick,
    AdapterFavorite.OnFavoriteClick, ChapterContract.ChapterView {

    private lateinit var rootFavorites: View

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterPresenter: ChapterPresenter

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var favoriteList: MutableList<ModelFavorite>
    private lateinit var adapterFavorite: AdapterFavorite

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootFavorites = inflater.inflate(R.layout.bottom_sheet_favorites, container, false)

        database = DatabaseOpenHelper(context).readableDatabase
        favoriteList = DatabaseLists(context).getFavroiteList

        chapterPresenter = ChapterPresenter(this, database)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        val verticalList = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootFavorites.rvFavorites.layoutManager = verticalList

        adapterFavorite = AdapterFavorite(favoriteList, this, this, preferences)
        rootFavorites.rvFavorites.adapter = adapterFavorite

        if (adapterFavorite.itemCount <= 0) {
            rootFavorites.isFavoriteEmpty.visibility = View.VISIBLE
            rootFavorites.rvFavorites.visibility = View.GONE
        } else {
            rootFavorites.isFavoriteEmpty.visibility = View.GONE
            rootFavorites.rvFavorites.visibility = View.VISIBLE
        }

        return rootFavorites
    }

    override fun itemClick(favoriteId: Int) {

    }

    override fun favoriteClick(state: Boolean, favoriteId: Int) {
        chapterPresenter.getFavorite(state, favoriteId)
    }

    override fun saveFavorite(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    override fun saveMessage(state: Boolean) {
        if (state) {
            Toast.makeText(context, getString(R.string.action_favorite_added), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, getString(R.string.action_favorite_removed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, "${getString(R.string.action_exception)} $message", Toast.LENGTH_SHORT).show()
    }
}