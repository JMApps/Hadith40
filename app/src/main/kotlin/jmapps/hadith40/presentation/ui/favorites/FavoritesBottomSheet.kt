package jmapps.hadith40.presentation.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import kotlinx.android.synthetic.main.bottom_sheet_favorites.view.*

class FavoritesBottomSheet : BottomSheetDialogFragment(), AdapterFavorite.OnItemClick {

    private lateinit var rootFavorites: View

    private lateinit var favoriteList: MutableList<ModelFavorite>
    private lateinit var adapterFavorite: AdapterFavorite

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootFavorites = inflater.inflate(R.layout.bottom_sheet_favorites, container, false)

        favoriteList = DatabaseLists(context).getFavroiteList

        val verticalList = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootFavorites.rvFavorites.layoutManager = verticalList

        adapterFavorite = AdapterFavorite(favoriteList, this)
        rootFavorites.rvFavorites.adapter = adapterFavorite

        if (adapterFavorite.itemCount <= 0) {
            rootFavorites.tvIfFavoriteListEmpty.visibility = View.VISIBLE
            rootFavorites.rvFavorites.visibility = View.GONE
        } else {
            rootFavorites.tvIfFavoriteListEmpty.visibility = View.GONE
            rootFavorites.rvFavorites.visibility = View.VISIBLE
        }

        return rootFavorites
    }

    override fun itemClick(favoriteId: Int) {
        Toast.makeText(context, "Click = $favoriteId", Toast.LENGTH_LONG).show()
    }
}