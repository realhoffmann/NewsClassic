package at.technikum_wien.polzert.newsclassic.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import at.technikum_wien.polzert.newsclassic.adapter.ListAdapter
import at.technikum_wien.polzert.newsclassic.databinding.ActivityMainBinding
import at.technikum_wien.polzert.newsclassic.viewmodels.NewsListViewModel
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.allViews
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import at.technikum_wien.polzert.newsclassic.R
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore("Settings")

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    val logTag = "MainActivity"
    val showImages = booleanPreferencesKey("showImages")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel by viewModels<NewsListViewModel>()

        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
        val adapter = ListAdapter()
        binding.rvList.adapter = adapter
        adapter.itemClickListener = {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.ITEM_KEY, it)
            startActivity(intent)
        }

        binding.btnReload.setOnClickListener {
            viewModel.reload()
        }

        viewModel.error.observe(this) {
            binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.newsItems.observe(this) {
            adapter.items = it
        }

        viewModel.busy.observe(this) {
            binding.btnReload.isEnabled = !it
        }

        binding.settingsButton.setOnClickListener {
            Intent(applicationContext, SettingsActivity::class.java)
                .also { startActivity(it) }

            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val signature = prefs.getString("signature", "")
            if (signature != null) {
                Log.i(logTag, "Signature : $signature")
            }

            prefs.registerOnSharedPreferenceChangeListener(this)

            lifecycleScope.launch {
                applicationContext.dataStore.edit {
                    it[showImages] = true
                    val currentValue = it[showImages]
                }
            }
            lifecycleScope.launch {
                val dataStorePrefs = applicationContext
                    .dataStore.data.first()

                val currentValue = dataStorePrefs[showImages]
            }
        }
    }
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (key == "showImages") {
                val showImages = sharedPreferences?.getBoolean(key, false)
                Log.i(
                    logTag,
                    "Show images: $showImages")

                if(showImages == true){
                    binding.rvList.allViews.forEach {
                        if(it is ImageView) {
                            it.visibility = View.GONE
                        }
                    }
                } else {
                    binding.rvList.allViews.forEach {
                        if(it is ImageView) {
                            it.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.menuitem_settings) {
                Intent(applicationContext, SettingsActivity::class.java)
                    .also { startActivity(it) }
                return true
            }
            if(item.itemId == R.id.btn_reload) {
                NewsListViewModel().reload()
                return true
            }
            return super.onOptionsItemSelected(item)

            }
        }

