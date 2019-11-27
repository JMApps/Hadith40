package jmapps.hadith40.presentation.ui.favorites

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class AdapterFavorite(private val favoriteList: MutableList<ModelFavorite>,
                      private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<ViewHolderFavorite>() {

    interface OnItemClick {
        fun itemClick(favoriteId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFavorite {
        return ViewHolderFavorite(LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite, parent, false))
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFavorite, position: Int) {
        val favoriteId = favoriteList[position].favoriteId
        val strChapterNumber = favoriteList[position].strNumberHadeeth
        val strFavoriteTitle = favoriteList[position].strFavoriteTitle

        holder.tvFavoriteNumber.text = strChapterNumber
        holder.tvFavoriteTitle.text = Html.fromHtml(strFavoriteTitle)

        holder.findItemClick(onItemClick, favoriteId!!)
    }
}