package jmapps.hadith40.presentation.mvp.main

import androidx.appcompat.app.AppCompatDelegate

class MainModel : MainContract.Model {

    override fun setNightMode(state: Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}