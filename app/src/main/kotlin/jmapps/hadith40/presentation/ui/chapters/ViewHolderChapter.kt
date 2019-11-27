package jmapps.hadith40.presentation.ui.chapters

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class ViewHolderChapter (itemView: View): RecyclerView.ViewHolder(itemView) {

    val tbChapterFavorite: ToggleButton = itemView.findViewById(R.id.tbChapterFavorite)
    val tvChapterNumber: TextView = itemView.findViewById(R.id.tvChapterNumber)
    val tvChapterTitle: TextView = itemView.findViewById(R.id.tvChapterTitle)

    fun findItemClick(onItemClick: AdapterChapter.OnItemClick, chapterId: Int) {
        itemView.setOnClickListener {
            onItemClick.itemClick(chapterId)
        }
    }
}