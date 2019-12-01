package jmapps.hadith40.presentation.ui.apart

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class AdapterApart(private val apartList: MutableList<ModelApart>,
                   private val apartItemClick: ApartItemClick): RecyclerView.Adapter<ViewHolderApart>() {

    interface ApartItemClick {
        fun itemClick(apartId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderApart {
        return ViewHolderApart(LayoutInflater.from(parent.context).inflate(
            R.layout.item_apart, parent, false))
    }

    override fun getItemCount(): Int {
        return apartList.size
    }

    override fun onBindViewHolder(holder: ViewHolderApart, position: Int) {
        val apartId = apartList[position].apartId
        val strContentArabic = apartList[position].strContentArabic
        val strContentTranslation = apartList[position].strContentTranslation

        holder.tvContentArabic.text = strContentArabic
        holder.tvContentTranslation.text = Html.fromHtml(strContentTranslation)

        holder.findApartItemClick(apartItemClick, apartId!!)
    }
}