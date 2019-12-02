package jmapps.hadith40.presentation.ui.apart

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class ViewHolderApart(itemView: View): RecyclerView.ViewHolder(itemView) {

    val cvApartItem: CardView = itemView.findViewById(R.id.cvApartItem)
    val tvContentArabic: TextView = itemView.findViewById(R.id.tvContentArabic)
    val tvContentTranslation: TextView = itemView.findViewById(R.id.tvContentTranslation)

    fun findApartItemClick (apartItemClick: AdapterApart.ApartItemClick, position: Int) {
        itemView.setOnClickListener {
            apartItemClick.itemClick(position)
        }
    }
}