package jmapps.hadith40.presentation.ui.chapters

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R

class AdapterChapter(private var chapterList: MutableList<ModelChapter>,
                     private val onItemClick: OnItemClick,
                     private val onFavoriteClick: OnFavoriteClick,
                     private val preferences: SharedPreferences) :
    RecyclerView.Adapter<ViewHolderChapter>(), Filterable {

    private var mainChapterList: MutableList<ModelChapter>? = null

    init {
        mainChapterList = chapterList
    }

    interface OnItemClick {
        fun itemClick(chapterId: Int)
    }

    interface OnFavoriteClick {
        fun favoriteClick(state: Boolean, chapterId: Int)
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

        holder.tbChapterFavorite.setOnCheckedChangeListener(null)
        holder.tbChapterFavorite.isChecked = preferences.getBoolean("key_chapter_favorite_$chapterId", false)
        holder.findItemClick(onItemClick, chapterId!!)
        holder.findFavoriteButton(onFavoriteClick, chapterId)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                chapterList = if (charString.isEmpty()) {
                    mainChapterList as MutableList<ModelChapter>
                } else {
                    val filteredList = ArrayList<ModelChapter>()
                    for (row in mainChapterList!!) {
                        if (row.strChapterTitle?.toLowerCase()!!.contains(charString.toLowerCase()) ||
                            row.strNumberHadeeth?.toLowerCase()!!.contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = chapterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                chapterList = filterResults.values as ArrayList<ModelChapter>
                notifyDataSetChanged()
            }
        }
    }
}