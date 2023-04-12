package at.technikum_wien.polzert.newsclassic.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import at.technikum_wien.polzert.newsclassic.NewsItemApplication
import at.technikum_wien.polzert.newsclassic.data.download.NewsItemRepository
import at.technikum_wien.polzert.newsclassic.worker.DownloadWorker
import kotlinx.coroutines.launch

    class NewsListViewModel(val workManager: WorkManager,
                            application: Application,
                            private val newsItemRepository: NewsItemRepository) :
                            AndroidViewModel(application) {

    private val _error = MutableLiveData(false)
    private val _busy = MutableLiveData(true)
    val newsItems = newsItemRepository.newsItems
    private var count = 0
    var url = ""

    init {
        reload(url)
    }

    val error: LiveData<Boolean>
        get() = _error
    val busy: LiveData<Boolean>
        get() = _busy

    private fun downloadNewsItems(newsFeedUrl: String) {
        scheduleBackgroundWork()
        _error.value = false
        _busy.value = true
        viewModelScope.launch {
            val value = newsItemRepository.loadNewsItems(newsFeedUrl, true)
            if (value) {
                _error.value = true
            } else {
                _busy.value = false
            }
        }
    }

        private fun scheduleBackgroundWork() {
            val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setConstraints(Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                    requiresBatteryNotLow = true
                ))
                .build()
            workManager.enqueue(workRequest)
        }

        fun reload(url: String) {
        if (url != "") {
            downloadNewsItems(url)
        } else {

            if (count % 2 == 0) {
                downloadNewsItems("https://www.engadget.com/rss.xml")
            } else {
                downloadNewsItems("https://www.derstandard.at/rss")
            }
            count++
        }
    }
}

    class NewsItemViewModelFactory(
        private val workManager: WorkManager,
        private val newsItemRepository: NewsItemRepository,
        private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsListViewModel(workManager, application, newsItemRepository) as T
            }
            throw IllegalArgumentException("Invalid viewModel class")
        }
}

