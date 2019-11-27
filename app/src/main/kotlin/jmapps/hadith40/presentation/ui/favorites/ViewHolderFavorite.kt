package jmapps.hadith40.presentation.ui.favorites

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class ViewHolderFavorite (itemView: View): RecyclerView.ViewHolder(itemView) {

    val tbFavorite: ToggleButton = itemView.findViewById(R.id.tbFavorite)
    val tvFavoriteNumber: TextView = itemView.findViewById(R.id.tvFavoriteNumber)
    val tvFavoriteTitle: TextView = itemView.findViewById(R.id.tvFavoriteTitle)

    fun findItemClick(onItemClick: AdapterFavorite.OnItemClick, favoriteId: Int) {
        itemView.setOnClickListener {
            onItemClick.itemClick(favoriteId)
        }
    }
}