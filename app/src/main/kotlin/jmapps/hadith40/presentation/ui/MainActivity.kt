package jmapps.hadith40.presentation.ui

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import jmapps.hadith40.R
import jmapps.hadith40.data.database.DatabaseLists
import jmapps.hadith40.data.database.DatabaseOpenHelper
import jmapps.hadith40.presentation.mvp.chapters.ChapterContract
import jmapps.hadith40.presentation.mvp.chapters.ChapterPresenter
import jmapps.hadith40.presentation.mvp.main.MainContract
import jmapps.hadith40.presentation.mvp.main.MainPresenter
import jmapps.hadith40.presentation.ui.about.AboutUsBottomSheet
import jmapps.hadith40.presentation.ui.chapters.AdapterChapter
import jmapps.hadith40.presentation.ui.chapters.ModelChapter
import jmapps.hadith40.presentation.ui.contents.ContentActivity
import jmapps.hadith40.presentation.ui.favorites.FavoritesBottomSheet
import jmapps.hadith40.presentation.ui.settings.SettingsBottomSheet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainContract.View, AdapterChapter.OnItemClick, View.OnClickListener,
    ChapterContract.ChapterView, AdapterChapter.OnFavoriteClick, SearchView.OnQueryTextListener {

    private var keyNightMode = "key_night_mode"

    private var database: SQLiteDatabase? = null

    private var mainPresenter: MainPresenter? = null
    private var chapterPresenter: ChapterPresenter? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var chapterList: MutableList<ModelChapter>
    private lateinit var adapterChapter: AdapterChapter

    private lateinit var swNightMode: Switch
    private lateinit var searchView: SearchView

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        LockOrientation(this).lock()

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        database = DatabaseOpenHelper(this).readableDatabase

        mainPresenter = MainPresenter(this, this)
        chapterPresenter = ChapterPresenter(this, database!!)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        navigationView.menu.findItem(R.id.nav_night_mode).actionView = Switch(this)
        swNightMode = navigationView.menu.findItem(R.id.nav_night_mode).actionView as Switch
        swNightMode.isClickable = false
        swNightMode.isChecked = preferences.getBoolean(keyNightMode, false)

        initView()

        rvMainChapters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx < dy) {
                    fabFavorites.hide()
                } else {
                    fabFavorites.show()
                }
            }
        })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            adapterChapter.filter.filter("")
        } else {
            adapterChapter.filter.filter(newText)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_settings -> mainPresenter?.getSettings()

            R.id.nav_night_mode -> {
                mainPresenter?.getNightMode(!swNightMode.isChecked)
                swNightMode.isChecked = !swNightMode.isChecked
            }

            R.id.nav_about_us -> mainPresenter?.getAboutUs()

            R.id.nav_rate -> mainPresenter?.getRate()

            R.id.nav_share -> mainPresenter?.getShare()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun initView() {
        mainPresenter?.getNightMode(swNightMode.isChecked)
        chapterList = DatabaseLists(this).getChapterList

        val verticalList = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMainChapters.layoutManager = verticalList

        adapterChapter = AdapterChapter(chapterList, this, this, preferences)
        rvMainChapters.adapter = adapterChapter

        fabFavorites.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        mainPresenter?.getFavorites()
    }

    override fun itemClick(chapterId: Int) {
        toContentActivity(chapterId)
    }

    override fun favoriteClick(state: Boolean, chapterId: Int) {
        chapterPresenter?.getFavorite(state, chapterId)
    }

    override fun setFavorites() {
        val favoritesBottomSheet = FavoritesBottomSheet()
        favoritesBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        favoritesBottomSheet.show(supportFragmentManager, "favorites")
    }

    override fun setSettings() {
        val settingsBottomSheet = SettingsBottomSheet()
        settingsBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        settingsBottomSheet.show(supportFragmentManager, "settings")
    }

    override fun saveNightModeState(state: Boolean) {
        editor.putBoolean(keyNightMode, state).apply()
    }

    override fun setAboutUs() {
        val aboutUsBottomSheet = AboutUsBottomSheet()
        aboutUsBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        aboutUsBottomSheet.show(supportFragmentManager, "about_us")
    }

    override fun saveFavorite(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    override fun saveMessage(state: Boolean) {
        if (state) {
            Toast.makeText(this, getString(R.string.action_favorite_added), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.action_favorite_removed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, "${getString(R.string.action_exception)} $message", Toast.LENGTH_SHORT).show()
    }

    private fun toContentActivity(chapterId: Int) {
        val toContent = Intent(this, ContentActivity::class.java)
        toContent.putExtra("key_chapter_position", chapterId)
        startActivity(toContent)
    }
}