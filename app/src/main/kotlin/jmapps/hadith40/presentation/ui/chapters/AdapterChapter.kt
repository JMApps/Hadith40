package jmapps.hadith40.presentation.ui.chapters

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class AdapterChapter(private val chapterList: MutableList<ModelChapter>,
                     private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<ViewHolderChapter>() {

    interface OnItemClick {
        fun itemClick(chapterId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChapter {
        return ViewHolderChapter(LayoutInflater.from(parent.context).inflate(
            R.layout.item_chapter, parent, false))
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolderChapter, position: Int) {
        val chapterId = chapterList[position].chapterId
        val strChapterNumber = chapterList[position].strNumberHadeeth
        val strChapterTitle = chapterList[position].strChapterTitle

        holder.tvChapterNumber.text = strChapterNumber
        holder.tvChapterTitle.text = Html.fromHtml(strChapterTitle)

        holder.findItemClick(onItemClick, chapterId!!)
    }
}