package jmapps.hadith40.presentation.mvp.main

class MainPresenter(_view: MainContract.View) : MainContract.Presenter {

    private var view: MainContract.View? = _view
    private var model: MainContract.Model = MainModel()

    init {
        view?.initView()
    }

    override fun getNightMode(state: Boolean) {
        model.setNightMode(state)
        view?.saveNigtModeState(state)
    }
}