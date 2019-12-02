package jmapps.hadith40.presentation.ui.contents

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.data.database.DatabaseOpenHelper
import jmapps.hadith40.presentation.mvp.content.ContentContract
import jmapps.hadith40.presentation.mvp.content.ContentPresenterImpl
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import kotlinx.android.synthetic.main.fragment_content.view.*

class ContentFragment : Fragment(), ContentContract.ContentView {

    private lateinit var rootContent: View

    private var sectionNumber: Int? = null
    private var database: SQLiteDatabase? = null
    private lateinit var chapterList: MutableList<ModelChapter>

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var contentPresenterImpl: ContentPresenterImpl

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

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootContent = inflater.inflate(R.layout.fragment_content, container, false)

        sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER)
        database = DatabaseOpenHelper(context).readableDatabase
        chapterList = DatabaseLists(context).getChapterList

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        contentPresenterImpl = ContentPresenterImpl(this, sectionNumber, database)
        contentPresenterImpl.initDatabaseContent()

        return rootContent
    }

    override fun initDatabaseContent(hadeethArabic: String, hadeethTranslation: String) {
        rootContent.tvHadeethArabic.text = Html.fromHtml(hadeethArabic)
        rootContent.tvHadeethTranslation.text = Html.fromHtml(hadeethTranslation)
    }

    override fun databaseException(e: Exception) {
       Toast.makeText(context, "${getString(R.string.action_exception)} $e", Toast.LENGTH_SHORT).show()
    }
}