package jmapps.hadith40.presentation.mvp.main

interface MainContract {

    interface Model {
        fun setNightMode(state: Boolean)
    }

    interface View {
        fun initView()
        fun setSettings()
        fun saveNightModeState(state: Boolean)
        fun setAboutUs()
    }

    interface Presenter {
        fun getSettings()
        fun getNightMode(state: Boolean)
        fun getAboutUs()
        fun getRate()
        fun getShare()
    }
}