package jmapps.hadith40.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.hadith40.R

class SettingsBottomSheet: BottomSheetDialogFragment() {

    private lateinit var rootSettings: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.bottom_sheet_settings, container, false)

        dialog?.setOnDismissListener {
            dialog?.dismiss()
        }
        return rootSettings
    }
}