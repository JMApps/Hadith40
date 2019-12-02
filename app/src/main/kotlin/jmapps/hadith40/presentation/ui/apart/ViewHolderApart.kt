package jmapps.hadith40.presentation.ui.apart

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class ViewHolderApart(itemView: View): RecyclerView.ViewHolder(itemView) {

    val cvApartItem: CardView = itemView.findViewById(R.id.cvApartItem)
    val tvApartNumberId: TextView = itemView.findViewById(R.id.tvApartNumberId)
    val tvContentArabic: TextView = itemView.findViewById(R.id.tvContentArabic)
    val tvContentTranslation: TextView = itemView.findViewById(R.id.tvContentTranslation)

    private var textSizeArabic: Int? = null
    private var textSizeArabicTranslation: Int? = null

    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)

    init {
        sizeArabic()
        sizeTranslation()
        showTranslation()
    }

    fun findApartItemClick (apartItemClick: AdapterApart.ApartItemClick, position: Int) {
        itemView.setOnClickListener {
            apartItemClick.itemClick(position)
        }
    }

    private fun sizeArabic() {
        textSizeArabic = preferences.getInt("key_size_arabic", 2)

        when (textSizeArabic) {

            0 -> textSizeArabic = 14
            1 -> textSizeArabic = 16
            2 -> textSizeArabic = 18
            3 -> textSizeArabic = 20
            4 -> textSizeArabic = 22
            5 -> textSizeArabic = 24
            6 -> textSizeArabic = 26
            7 -> textSizeArabic = 28
            8 -> textSizeArabic = 30
        }
        tvContentArabic.textSize = textSizeArabic!!.toFloat()
    }

    private fun sizeTranslation() {
        textSizeArabicTranslation = preferences.getInt("key_size_transcription", 2)

        when (textSizeArabicTranslation) {

            0 -> textSizeArabicTranslation = 14
            1 -> textSizeArabicTranslation = 16
            2 -> textSizeArabicTranslation = 18
            3 -> textSizeArabicTranslation = 20
            4 -> textSizeArabicTranslation = 22
            5 -> textSizeArabicTranslation = 24
            6 -> textSizeArabicTranslation = 26
            7 -> textSizeArabicTranslation = 28
            8 -> textSizeArabicTranslation = 30
        }
        tvContentTranslation.textSize = textSizeArabicTranslation!!.toFloat()
    }

    private fun showTranslation() {
        if (preferences.getBoolean("key_show_translation", true)) {
            tvContentTranslation.visibility = View.VISIBLE
        } else {
            tvContentTranslation.visibility = View.GONE
        }
    }
}