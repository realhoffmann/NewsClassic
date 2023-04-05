package at.technikum_wien.polzert.newsclassic.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import at.technikum_wien.polzert.newsclassic.NewsItemApplication
import at.technikum_wien.polzert.newsclassic.R
import at.technikum_wien.polzert.newsclassic.adapter.ListAdapter
import at.technikum_wien.polzert.newsclassic.data.download.NewsDownloader
import at.technikum_wien.polzert.newsclassic.data.download.NewsItemRepository
import at.technikum_wien.polzert.newsclassic.databinding.ActivityMainBinding
import at.technikum_wien.polzert.newsclassic.viewmodels.NewsItemViewModelFactory
import at.technikum_wien.polzert.newsclassic.viewmodels.NewsListViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore("Settings")

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    val logTag = "MainActivity"
    val showImages = booleanPreferencesKey("showImages")
    var url = ""

    val viewModel: NewsListViewModel by viewModels(
        factoryProducer = {
            val application = applicationContext as NewsItemApplication
            NewsItemViewModelFactory(newsItemRepository(application), application)
        }
    )
    private fun newsItemRepository(application: NewsItemApplication): NewsItemRepository {
        val dao = application.appRoomDatabase.newsItemDao()
        val downloader = NewsDownloader()
        return NewsItemRepository(dao, downloader)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            viewModel.reload(url)
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
                .also {
                    startActivity(it)
                }
        }

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
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == "signature"){
            val signature = sharedPreferences?.getString(key, "")
            Log.i(logTag, "Signature: $signature")
            url = signature.toString()
            viewModel.reload(url)
        }

        if (key == "showImages") {
            val showImages = sharedPreferences?.getBoolean(key, true)
            Log.i(logTag, "Show images: $showImages")
            val imageAdapter = binding.rvList.adapter as ListAdapter
            imageAdapter.showImages = showImages?: true
            imageAdapter.notifyDataSetChanged()
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
            val viewModel by viewModels<NewsListViewModel>()
            viewModel.reload(url)
            return true
        }
        return super.onOptionsItemSelected(item)

    }
}
