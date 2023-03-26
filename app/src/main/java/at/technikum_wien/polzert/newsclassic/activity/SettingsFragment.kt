package at.technikum_wien.polzert.newsclassic.activity

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import at.technikum_wien.polzert.newsclassic.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
