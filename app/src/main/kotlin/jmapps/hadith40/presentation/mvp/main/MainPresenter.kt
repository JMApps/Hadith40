package jmapps.hadith40.presentation.mvp.main

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import jmapps.hadith40.R

class MainPresenter(private val context: Context?, _view: MainContract.View) : MainContract.Presenter {

    private var view: MainContract.View? = _view
    private var model: MainContract.Model = MainModel()

    init {
        view?.initView()
    }

    override fun getSettings() {
        view?.setSettings()
    }

    override fun getNightMode(state: Boolean) {
        model.setNightMode(state)
        view?.saveNightModeState(state)
    }

    override fun getAboutUs() {
        view?.setAboutUs()
    }

    override fun getRate() {
        val rate = Intent(Intent.ACTION_VIEW)
        rate.data = context?.getString(R.string.action_app_link)?.toUri()
        context?.startActivity(rate)
    }

    override fun getShare() {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT,
            "${context?.getString(R.string.action_app_full_name)}\n" +
                    "${context?.getString(R.string.action_app_link)}")
        context?.startActivity(shareLink)
    }
}