package jmapps.hadith40.presentation.mvp.main

interface MainContract {

    interface Model {
        fun setNightMode(state: Boolean)
    }

    interface View {
        fun initView()
        fun saveNigtModeState(state: Boolean)
    }

    interface Presenter {
        fun getNightMode(state: Boolean)
    }
}