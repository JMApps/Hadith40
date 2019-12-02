package jmapps.hadith40.presentation.ui.contents

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseContent
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.data.database.DatabaseOpenHelper
import jmapps.hadith40.presentation.mvp.chapters.ChapterContract
import jmapps.hadith40.presentation.mvp.chapters.ChapterPresenter
import jmapps.hadith40.presentation.ui.LockOrientation
import jmapps.hadith40.presentation.ui.apart.ApartActivity
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity(), ViewPager.OnPageChangeListener,
    View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener,
    ChapterContract.ChapterView, MediaPlayer.OnCompletionListener {

    private var database: SQLiteDatabase? = null

    private lateinit var chapterList: MutableList<ModelChapter>
    private lateinit var contentList: MutableList<ModelContent>

    private var chapterPresenter: ChapterPresenter? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var player: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    private var trackIndex: Int = 0
    private var chapterPosition: Int? = null

    private var numberHadeeth: String? = null
    private var chapterTitle: String? = null
    private var contentArabic: String? = null
    private var contentTranslation: String? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        setSupportActionBar(toolbar)

        LockOrientation(this).lock()

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        database = DatabaseOpenHelper(this).readableDatabase

        chapterList = DatabaseLists(this).getChapterList
        contentList = DatabaseContent(this).getContentList

        chapterPresenter = ChapterPresenter(this, database!!)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mainViewPager.adapter = sectionsPagerAdapter

        chapterPosition = intent.getIntExtra("key_chapter_position", 0)
        trackIndex = chapterPosition as Int - 1
        mainViewPager.currentItem = chapterPosition!! - 1

        numberHadeeth = chapterList[chapterPosition!! - 1].strNumberHadeeth
        chapterTitle = chapterList[chapterPosition!! - 1].strChapterTitle

        contentArabic = contentList[chapterPosition!! - 1].strContentArabic
        contentTranslation = contentList[chapterPosition!! - 1].strContentTranslation

        tvChapterNumber.text = numberHadeeth
        tvChapterTitle.text = chapterTitle

        mainViewPager.addOnPageChangeListener(this)

        fabApartList.setOnClickListener(this)
        btnPrevious.setOnClickListener(this)
        tbPlay.setOnCheckedChangeListener(this)
        btnNext.setOnClickListener(this)
        sbAudioProgress.setOnSeekBarChangeListener(this)
        tbLoop.setOnCheckedChangeListener(this)

        tbChapterFavorite.isChecked =
            preferences.getBoolean("key_chapter_favorite_${chapterPosition}", false)
        btnShareContent.setOnClickListener(this)
        tbChapterFavorite.setOnCheckedChangeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        chapterPosition = position + 1
        trackIndex = position

        numberHadeeth = chapterList[position].strNumberHadeeth
        chapterTitle = chapterList[position].strChapterTitle

        contentArabic = contentList[position].strContentArabic
        contentTranslation = contentList[position].strContentTranslation

        tvChapterNumber.text = numberHadeeth
        tvChapterTitle.text = chapterTitle

        tbPlay.isChecked = false
        tbLoop.isChecked = false
        clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.fabApartList -> {
                val toApartList = Intent(this, ApartActivity::class.java)
                toApartList.putExtra("key_apart_position", chapterPosition)
                startActivity(toApartList)
            }

            R.id.btnPrevious -> {
                if (trackIndex > 0) {
                    trackIndex--
                    initPlayer(trackIndex)
                    mainViewPager.currentItem = trackIndex
                }
            }

            R.id.btnNext -> {
                if (trackIndex < chapterList.size - 1) {
                    trackIndex++
                    initPlayer(trackIndex)
                    mainViewPager.currentItem = trackIndex
                }
            }

            R.id.btnShareContent -> {
                val shareContent = Intent(Intent.ACTION_SEND)
                shareContent.type = "text/plain"
                shareContent.putExtra(
                    Intent.EXTRA_TEXT, Html.fromHtml(
                        "$numberHadeeth<br/>$chapterTitle<p/>" +
                                "$contentArabic<p/>$contentTranslation"
                    ).toString()
                )
                startActivity(shareContent)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.tbChapterFavorite -> chapterPresenter?.getFavorite(isChecked, chapterPosition!!)

            R.id.tbPlay -> {
                if (isChecked) {
                    if (player == null) {
                        initPlayer(trackIndex)
                        player?.start()
                    } else {
                        currentAudioProgress()
                        player?.start()
                    }
                } else {
                    player?.pause()
                }
            }

            R.id.tbLoop -> {
                if (isChecked) {
                    Toast.makeText(this, getString(R.string.player_loop_on), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, getString(R.string.player_loop_off), Toast.LENGTH_SHORT)
                        .show()
                }
                player?.isLooping = isChecked
            }
        }
    }

    override fun saveFavorite(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    override fun saveMessage(state: Boolean) {
        if (state) {
            Toast.makeText(this, getString(R.string.action_favorite_added), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.action_favorite_removed), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, "${getString(R.string.action_exception)} $message", Toast.LENGTH_SHORT)
            .show()
    }

    private fun initPlayer(index: Int) {
        clear()
        val resId = resources?.getIdentifier(
            chapterList[index].strNameAudio, "raw", "jmapps.hadith40"
        )
        player = resId?.let { MediaPlayer.create(this, it) }
        currentAudioProgress()
        player?.setOnCompletionListener(this)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        tbPlay.isChecked = false
        handler.removeCallbacks(runnable)
        sbAudioProgress?.progress = 0
    }

    private fun currentAudioProgress() {
        sbAudioProgress?.max = player?.seconds!!
        runnable = Runnable {
            if (player != null) {
                sbAudioProgress?.progress = player?.currentSeconds!!
                handler.postDelayed(runnable, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player?.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun clear() {
        if (player != null) {
            handler.removeCallbacks(runnable)
            sbAudioProgress?.progress = 0
            player?.stop()
            player?.release()
            player = null
        }
    }

    private val MediaPlayer.seconds: Int?
        get() {
            return this.duration / 1000
        }

    private val MediaPlayer.currentSeconds: Int?
        get() {
            return this.currentPosition / 1000
        }
}