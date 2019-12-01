package jmapps.hadith40.presentation.ui.contents

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.presentation.ui.apart.ApartBottomSheet
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private lateinit var chapterList: MutableList<ModelChapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val chapterPosition = intent.getIntExtra("key_chapter_position", 0)
        chapterList = DatabaseLists(this).getChapterList

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mainViewPager.adapter = sectionsPagerAdapter

        tvChapterNumber.text = chapterList[chapterPosition - 1].strNumberHadeeth
        tvChapterTitle.text = chapterList[chapterPosition - 1].strChapterTitle
        mainViewPager.currentItem = chapterPosition - 1

        appBar.addOnOffsetChangedListener(this)
        mainViewPager.addOnPageChangeListener(this)
        fabApartContent.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        tvChapterNumber.text = chapterList[position].strNumberHadeeth
        tvChapterTitle.text = chapterList[position].strChapterTitle
    }

    override fun onOffsetChanged(appBar: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset < 0) {
            fabApartContent.hide()
        } else {
            fabApartContent.show()
        }
    }

    override fun onClick(v: View?) {
        val apartBottomSheet = ApartBottomSheet()
        apartBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        apartBottomSheet.show(supportFragmentManager, "apart_list")
    }
}
