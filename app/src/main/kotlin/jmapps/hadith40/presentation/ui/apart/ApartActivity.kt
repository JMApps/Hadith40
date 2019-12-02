package jmapps.hadith40.presentation.ui.apart

import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.hadith40.R
import jmapps.hadith40.data.database.ApartList
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.presentation.ui.LockOrientation
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import kotlinx.android.synthetic.main.activity_apart.*
import kotlinx.android.synthetic.main.activity_content.toolbar
import kotlinx.android.synthetic.main.content_apart.*

class ApartActivity : AppCompatActivity(), AdapterApart.ApartItemClick, View.OnClickListener,
    MediaPlayer.OnCompletionListener {

    private lateinit var apartList: MutableList<ModelApart>
    private lateinit var adapterApart: AdapterApart

    private lateinit var chapterList: MutableList<ModelChapter>

    private var mediaPlayer: MediaPlayer? = null
    private var trackPosition: Int = 0
    private var apartPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apart)
        setSupportActionBar(toolbar)

        LockOrientation(this).lock()

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        apartPosition = intent.getIntExtra("key_apart_position", 1)
        apartList = ApartList(this).getApartList(apartPosition!!)

        val verticalLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvApartContent.layoutManager = verticalLayout

        adapterApart = AdapterApart(this, apartList, this)
        rvApartContent.adapter = adapterApart

        chapterList = DatabaseLists(this).getChapterList
        tvChapterNumber.text = chapterList[apartPosition!! - 1].strNumberHadeeth
        tvChapterTitle.text = chapterList[apartPosition!! - 1].strChapterTitle

        rvApartContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx < dy) {
                    fabPlayApart.hide()
                } else {
                    fabPlayApart.show()
                }
            }
        })
        fabPlayApart.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }

    override fun itemClick(position: Int) {
        trackPosition = position
        playTrack()
        mediaPlayer?.setOnCompletionListener {
            adapterApart.onItemSelected(-1)
        }
    }

    override fun onClick(v: View?) {
        playTrack()
        mediaPlayer?.setOnCompletionListener(this)
    }

    private fun playTrack() {
        clear()
        val resId = resources?.getIdentifier(
            apartList[trackPosition].strNameAudio, "raw", "jmapps.hadith40"
        )
        mediaPlayer = MediaPlayer.create(this, resId!!)
        mediaPlayer?.start()
        adapterApart.onItemSelected(trackPosition)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (trackPosition <= apartList.size - 2) {
            trackPosition++
            playTrack()
            rvApartContent.smoothScrollToPosition(trackPosition + 2)
            mediaPlayer?.setOnCompletionListener(this)
        } else {
            trackPosition = 0
            adapterApart.onItemSelected(-1)
            rvApartContent.smoothScrollToPosition(trackPosition)
            clear()
        }
    }

    private fun clear() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}