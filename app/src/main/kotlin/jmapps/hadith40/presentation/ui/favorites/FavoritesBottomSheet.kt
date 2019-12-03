package jmapps.hadith40.presentation.ui.favorites

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.data.database.DatabaseOpenHelper
import jmapps.hadith40.presentation.ui.contents.ContentActivity
import kotlinx.android.synthetic.main.bottom_sheet_favorites.view.*

class FavoritesBottomSheet : BottomSheetDialogFragment(), AdapterFavorite.OnItemClick {

    private lateinit var rootFavorites: View

    private lateinit var database: SQLiteDatabase

    private lateinit var favoriteList: MutableList<ModelFavorite>
    private lateinit var adapterFavorite: AdapterFavorite

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootFavorites = inflater.inflate(R.layout.bottom_sheet_favorites, container, false)

        initView()

        return rootFavorites
    }

    override fun itemClick(favoriteId: Int) {
        toContentActivity(favoriteId)
    }

    @SuppressLint("CommitPrefEdits")
    private fun initView() {
        database = DatabaseOpenHelper(context).readableDatabase
        favoriteList = DatabaseLists(context).getFavroiteList

        val verticalList = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootFavorites.rvFavorites.layoutManager = verticalList

        adapterFavorite = AdapterFavorite(favoriteList, this)
        rootFavorites.rvFavorites.adapter = adapterFavorite

        if (adapterFavorite.itemCount <= 0) {
            rootFavorites.isFavoriteEmpty.visibility = View.VISIBLE
            rootFavorites.rvFavorites.visibility = View.GONE
        } else {
            rootFavorites.isFavoriteEmpty.visibility = View.GONE
            rootFavorites.rvFavorites.visibility = View.VISIBLE
        }

    }

    private fun toContentActivity(favoriteId: Int) {
        val toContent = Intent(context, ContentActivity::class.java)
        toContent.putExtra("key_chapter_position", favoriteId)
        context?.startActivity(toContent)
    }
}