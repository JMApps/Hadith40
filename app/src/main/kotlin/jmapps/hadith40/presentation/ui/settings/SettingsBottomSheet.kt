package jmapps.hadith40.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.hadith40.R
import kotlinx.android.synthetic.main.bottom_sheet_settings.view.*

class SettingsBottomSheet : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var rootSettings: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var textSizeValues: ArrayList<Int> = ArrayList()

    init {
        textSizeValues.add(14)
        textSizeValues.add(16)
        textSizeValues.add(18)
        textSizeValues.add(20)
        textSizeValues.add(22)
        textSizeValues.add(24)
        textSizeValues.add(26)
        textSizeValues.add(28)
        textSizeValues.add(30)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.bottom_sheet_settings, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        rootSettings.sbTextSizeArabic.progress = preferences.getInt("key_size_arabic", 2)
        rootSettings.sbTextSizeTranslation.progress =
            preferences.getInt("key_size_transcription", 2)
        rootSettings.swShowTranslation.isChecked =
            preferences.getBoolean("key_show_translation", true)

        rootSettings.tvValueSizeArabic.text =
            textSizeValues[preferences.getInt("key_size_arabic", 2)].toString()
        rootSettings.tvValueSizeTranslation.text =
            textSizeValues[preferences.getInt("key_size_transcription", 2)].toString()

        rootSettings.sbTextSizeArabic.setOnSeekBarChangeListener(this)
        rootSettings.sbTextSizeTranslation.setOnSeekBarChangeListener(this)
        rootSettings.swShowTranslation.setOnCheckedChangeListener(this)

        return rootSettings
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.sbTextSizeArabic -> {
                editor.putInt("key_size_arabic", progress).apply()
                rootSettings.tvValueSizeArabic.text = textSizeValues[progress].toString()
            }
            R.id.sbTextSizeTranslation -> {
                editor.putInt("key_size_transcription", progress).apply()
                rootSettings.tvValueSizeTranslation.text = textSizeValues[progress].toString()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.swShowTranslation -> editor.putBoolean("key_show_translation", isChecked).apply()
        }
    }
}