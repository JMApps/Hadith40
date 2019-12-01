package jmapps.hadith40.presentation.ui.apart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.hadith40.R
import jmapps.hadith40.data.database.ApartList
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import kotlinx.android.synthetic.main.bottom_sheet_apart.view.*

class ApartBottomSheet : BottomSheetDialogFragment(), AdapterApart.ApartItemClick {

    private lateinit var rootApart: View

    private lateinit var apartList: MutableList<ModelApart>
    private lateinit var adapterApart: AdapterApart

    private lateinit var chapterList: MutableList<ModelChapter>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootApart = inflater.inflate(R.layout.bottom_sheet_apart, container, false)

        apartList = ApartList(context).getApartList(1)

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootApart.rvApartContent.layoutManager = verticalLayout

        adapterApart = AdapterApart(apartList, this)
        rootApart.rvApartContent.adapter = adapterApart

        chapterList = DatabaseLists(context).getChapterList
        rootApart.tvHadeethNumber.text = chapterList[1].strNumberHadeeth
        rootApart.tvHadeethTitle.text = chapterList[1].strChapterTitle

        rootApart.rvApartContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx < dy) {
                    rootApart.fabPlayApart.hide()
                } else {
                    rootApart.fabPlayApart.show()
                }
            }
        })

        return rootApart
    }

    override fun itemClick(apartId: Int) {
        Toast.makeText(context, "Click = $apartId", Toast.LENGTH_SHORT).show()
    }
}