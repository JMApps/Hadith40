package jmapps.hadith40.presentation.ui.favorites

import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R
import jmapps.hadith40.presentation.mvp.chapters.ChapterPresenter
import jmapps.hadith40.presentation.ui.chapters.AdapterChapter

class AdapterFavorite(private val favoriteList: MutableList<ModelFavorite>,
                      private val onItemClick: OnItemClick,
                      private val onFavoriteClick: OnFavoriteClick,
                      private val preferences: SharedPreferences) :
    RecyclerView.Adapter<ViewHolderFavorite>() {

    interface OnItemClick {
        fun itemClick(favoriteId: Int)
    }

    interface OnFavoriteClick {
        fun favoriteClick(state: Boolean, favoriteId: Int)
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

        holder.tbFavorite.setOnCheckedChangeListener(null)
        holder.tbFavorite.isChecked = preferences.getBoolean("key_chapter_favorite_$favoriteId", false)
        holder.findItemClick(onItemClick, favoriteId!!)
        holder.findFavoriteButton(onFavoriteClick, favoriteId)
    }
}