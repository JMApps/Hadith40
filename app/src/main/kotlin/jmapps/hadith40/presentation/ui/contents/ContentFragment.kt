package jmapps.hadith40.presentation.ui.contents

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.data.database.DatabaseOpenHelper
import jmapps.hadith40.presentation.mvp.chapters.ChapterContract
import jmapps.hadith40.presentation.mvp.chapters.ChapterPresenter
import jmapps.hadith40.presentation.mvp.content.ContentContract
import jmapps.hadith40.presentation.mvp.content.ContentPresenterImpl
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import kotlinx.android.synthetic.main.fragment_content.view.*

class ContentFragment : Fragment(), ContentContract.ContentView, ChapterContract.ChapterView,
    CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    MediaPlayer.OnCompletionListener {

    private var sectionNumber: Int? = null
    private var database: SQLiteDatabase? = null
    private lateinit var chapterList: MutableList<ModelChapter>

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var contentPresenterImpl: ContentPresenterImpl
    private var chapterPresenter: ChapterPresenter? = null

    private var player: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): ContentFragment {
            return ContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private lateinit var rootContent: View

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootContent = inflater.inflate(R.layout.fragment_content, container, false)

        sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER)
        database = DatabaseOpenHelper(context).readableDatabase
        chapterList = DatabaseLists(context).getChapterList

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        contentPresenterImpl = ContentPresenterImpl(this, sectionNumber, database)
        chapterPresenter = ChapterPresenter(this, database!!)
        contentPresenterImpl.initDatabaseContent()

        rootContent.tbPlay.setOnCheckedChangeListener(this)
        rootContent.sbAudioProgress.setOnSeekBarChangeListener(this)
        rootContent.tbLoop.setOnCheckedChangeListener(this)

        rootContent.tbChapterFavorite.isChecked = preferences.getBoolean("key_chapter_favorite_${sectionNumber!!}", false)
        rootContent.btnShareContent.setOnClickListener(this)
        rootContent.tbChapterFavorite.setOnCheckedChangeListener(this)

        return rootContent
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.tbChapterFavorite -> chapterPresenter?.getFavorite(isChecked, sectionNumber!!)

            R.id.tbPlay -> {
                if (isChecked) {
                    if (player == null) {
                        initPlayer(sectionNumber!!)
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
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                }
                player?.isLooping = isChecked
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player?.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onClick(v: View?) {
        contentPresenterImpl.shareContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (player != null) {
            handler.removeCallbacks(runnable)
        }
        clear()
    }

    override fun initDatabaseContent(hadeethArabic: String, hadeethTranslation: String) {
        rootContent.tvHadeethArabic.text = Html.fromHtml(hadeethArabic)
        rootContent.tvHadeethTranslation.text = Html.fromHtml(hadeethTranslation)
    }

    override fun databaseException(e: Exception) {
       Toast.makeText(context, "${getString(R.string.action_exception)} $e", Toast.LENGTH_SHORT).show()
    }

    override fun shareContent(hadeethArabic: String, hadeethTranslation: String) {
        val shareContent = Intent(Intent.ACTION_SEND)
        shareContent.type = "text/plain"
        shareContent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(
                "${chapterList[sectionNumber!! - 1].strNumberHadeeth}<p/>$hadeethArabic<p/>$hadeethTranslation").toString())
        context?.startActivity(shareContent)
    }

    override fun saveFavorite(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    override fun saveMessage(state: Boolean) {
        if (state) {
            Toast.makeText(context, getString(R.string.action_favorite_added), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, getString(R.string.action_favorite_removed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, "${getString(R.string.action_exception)} $message", Toast.LENGTH_SHORT).show()
    }

    private fun initPlayer(index: Int) {
        clear()
        val resId = context?.resources?.getIdentifier(
            chapterList[index].strNameAudio, "raw", "jmapps.hadith40")
        player = resId?.let { MediaPlayer.create(context, it) }
        currentAudioProgress()
        player?.setOnCompletionListener(this)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        rootContent.tbPlay.isChecked = false
        handler.removeCallbacks(runnable)
        rootContent.sbAudioProgress?.progress = 0
    }

    private fun currentAudioProgress() {
        rootContent.sbAudioProgress?.max = player?.seconds!!
        runnable = Runnable {
            rootContent.sbAudioProgress?.progress = player?.currentSeconds!!
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun clear() {
        player?.stop()
        player?.release()
        player = null
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