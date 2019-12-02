package jmapps.hadith40.presentation.ui.apart

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class AdapterApart(
    private val context: Context?,
    private val apartList: MutableList<ModelApart>,
    private val apartItemClick: ApartItemClick) : RecyclerView.Adapter<ViewHolderApart>() {

    private var trackIndex = -1

    interface ApartItemClick {
        fun itemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderApart {
        return ViewHolderApart(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_apart, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return apartList.size
    }

    override fun onBindViewHolder(holder: ViewHolderApart, position: Int) {
        val strContentArabic = apartList[position].strContentArabic
        val strContentTranslation = apartList[position].strContentTranslation

        holder.tvApartNumberId.text = (position + 1).toString()
        holder.tvContentArabic.text = strContentArabic

        if (strContentTranslation.isNullOrEmpty()) {
            holder.tvContentTranslation.visibility = View.GONE
        } else {
            holder.tvContentTranslation.text = Html.fromHtml(strContentTranslation)
        }

        if (trackIndex == position) {
            holder.cvApartItem.setCardBackgroundColor(context?.resources!!.getColor(R.color.lightAccent))
        } else {
            holder.cvApartItem.setCardBackgroundColor(context?.resources!!.getColor(R.color.reMain))
        }

        holder.findApartItemClick(apartItemClick, position)
    }

    fun onItemSelected(trackIndex: Int) {
        this.trackIndex = trackIndex
        notifyDataSetChanged()
    }
}